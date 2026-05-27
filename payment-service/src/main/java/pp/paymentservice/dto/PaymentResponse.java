package pp.paymentservice.dto;

import lombok.Builder;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.paymentservice.entities.enums.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record PaymentResponse(
        Long paymentId,
        Long orderId,
        UUID userUuid,
        BigDecimal totalAmount,
        Currency currency,
        PaymentStatus status,
        Instant createdAt
) {}
