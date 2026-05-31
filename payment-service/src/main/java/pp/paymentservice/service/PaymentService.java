package pp.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.paymentservice.dto.PaymentRequest;
import pp.paymentservice.dto.PaymentDto;
import pp.paymentservice.entities.Payment;
import pp.paymentservice.kafka.publisher.PaymentCompletedEventPublisher;
import pp.paymentservice.repository.PaymentRepository;
import pp.paymentservice.utils.PaymentUtils;
import reactor.core.publisher.Mono;

import static pp.paymentservice.utils.PaymentUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentCompletedEventPublisher paymentCompletedEventPublisher;

    public Mono<Payment> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .switchIfEmpty(Mono.error(new RuntimeException("Payment for order with id: " + orderId + " not found")));
    }

    public Mono<PaymentDto> processPayment(String currentUserId, PaymentRequest request) {
        log.info("Start processing payment request for order id: {}", request.orderId());
        return Mono.defer(() ->
                paymentRepository.findByOrderId(request.orderId())
                        .flatMap(payment -> {
                            var paymentStatus = payment.getStatus();
                            if (!(paymentStatus == PaymentStatus.PENDING_PAYMENT ||
                                  paymentStatus == PaymentStatus.PAYMENT_FAILED)) {
                                log.warn("Cannot process payment because of payment status");
                                return Mono.error(new RuntimeException(
                                        "Can not process payment, payment status: " + paymentStatus));
                            }
                            return paymentRepository.save(payment);
                        })
                        .switchIfEmpty(Mono.defer(() ->
                                paymentRepository.save(buildPayment(currentUserId, request))
                        ))
                        .flatMap(payment -> {
                            log.info("Building PaymentCompletedEvent");
                            var paymentEvent = buildPaymentEvent(payment);
                            log.info("Publishing PaymentCompletedEvent");
                            return paymentCompletedEventPublisher
                                    .publishPaymentEvent(paymentEvent)
                                    .thenReturn(payment);
                        })
                        .map(PaymentUtils::buildPaymentResponse)
        );
    }

    public Mono<Void> save(Payment payment) {
        return paymentRepository.save(payment).then();
    }
}
