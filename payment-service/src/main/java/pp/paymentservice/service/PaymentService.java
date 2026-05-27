package pp.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.paymentservice.dto.PaymentRequest;
import pp.paymentservice.dto.PaymentResponse;
import pp.paymentservice.publisher.PaymentCompletedEventPublisher;
import pp.paymentservice.repository.PaymentRepository;
import pp.paymentservice.utils.PaymentUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentCompletedEventPublisher paymentCompletedEventPublisher;

    public Mono<PaymentResponse> processPayment(String currentUserId, PaymentRequest request) {
        return Mono.defer(() -> {
            var payment = PaymentUtils.buildPayment(currentUserId, request);

            if (!(payment.getStatus() == PaymentStatus.PENDING_PAYMENT)) {
                return Mono.error(new RuntimeException("Payment status is not PENDING_PAYMENT"));
            }

            return paymentRepository.save(payment);
        })
        .flatMap(payment -> {
            var paymentEvent = PaymentUtils.buildPaymentEvent(payment);
            return paymentCompletedEventPublisher.publishPaymentEvent(paymentEvent).thenReturn(payment);
        })
        .map(PaymentUtils::buildPaymentResponse);
    }
}
