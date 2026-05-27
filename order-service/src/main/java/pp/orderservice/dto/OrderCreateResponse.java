package pp.orderservice.dto;

import lombok.Builder;
import pp.commonlib.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderCreateResponse(
        Long orderId,
        UUID userUuid,
        Long restaurantId,
        String address,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemCreateResponse> items,
        Instant createdAt
)
{}
