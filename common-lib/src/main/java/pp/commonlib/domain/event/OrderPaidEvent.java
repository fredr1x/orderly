package pp.commonlib.domain.event;

import lombok.Builder;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.commonlib.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderPaidEvent(
        Long orderId,
        Long restaurantId,
        UUID userUuid,
        String address,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemDto> items
)
{}
