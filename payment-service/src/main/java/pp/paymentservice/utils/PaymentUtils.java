package pp.paymentservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.event.PaymentCompletedEvent;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.paymentservice.dto.PaymentRequest;
import pp.paymentservice.dto.PaymentResponse;
import pp.paymentservice.entities.Payment;

import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class PaymentUtils {

    public Payment buildPayment(String currentUserId, PaymentRequest request) {
        return Payment.builder()
                .orderId(request.orderId())
                .userUuid(UUID.fromString(currentUserId))
                .status(PaymentStatus.PAYMENT_SUCCEEDED)
                .totalAmount(request.totalAmount())
                .currency(request.currency())
                .createdAt(Instant.now())
                .build();
    }

    public PaymentCompletedEvent buildPaymentEvent(Payment payment) {
        return PaymentCompletedEvent.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userUuid(payment.getUserUuid())
                .totalAmount(payment.getTotalAmount())
                .status(payment.getStatus())
                .timestamp(Instant.now())
                .build();
    }

    public PaymentResponse buildPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userUuid(payment.getUserUuid())
                .totalAmount(payment.getTotalAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
