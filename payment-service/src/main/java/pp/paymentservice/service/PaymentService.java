package pp.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.commonlib.domain.outbox.EventType;
import pp.paymentservice.dto.PaymentDto;
import pp.paymentservice.dto.PaymentRequest;
import pp.paymentservice.entities.Payment;
import pp.paymentservice.repository.OutboxEventRepository;
import pp.paymentservice.repository.PaymentRepository;
import pp.paymentservice.utils.OutboxEventUtils;
import pp.paymentservice.utils.PaymentUtils;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OutboxEventUtils outboxEventUtils;
    private final OutboxEventRepository outboxEventRepository;
    private final TransactionalOperator transactionalOperator;

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
                                return Mono.error(new RuntimeException(
                                        "Can not process payment, payment status: " + paymentStatus));
                            }
                            payment.setStatus(PaymentStatus.PAYMENT_SUCCEEDED);
                            return paymentRepository.save(payment);
                        })
                        .switchIfEmpty(Mono.defer(() ->
                                paymentRepository.save(PaymentUtils.buildPayment(currentUserId, request))
                        ))
                        .flatMap(payment -> {
                            var paymentEvent = PaymentUtils.buildPaymentEvent(payment);
                            log.info("Publishing PaymentCompletedEvent");

                            var outboxEvent = outboxEventUtils.create(
                                    paymentEvent.orderId(),
                                    EventType.PAYMENT_COMPLETED_EVENT,
                                    paymentEvent
                            );
                            return outboxEventRepository.save(outboxEvent)
                                    .thenReturn(payment);
                        })
                        .map(PaymentUtils::buildPaymentResponse)
                        .as(transactionalOperator::transactional)
        );
    }

    public Mono<Void> save(Payment payment) {
        return paymentRepository.save(payment).then();
    }
}
