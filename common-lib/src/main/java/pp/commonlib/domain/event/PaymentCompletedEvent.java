package pp.commonlib.domain.event;

import lombok.Builder;
import pp.commonlib.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record PaymentCompletedEvent(
        Long paymentId,
        Long orderId,
        UUID userUuid,
        BigDecimal totalAmount,
        PaymentStatus status,
        Instant timestamp
)
{}
