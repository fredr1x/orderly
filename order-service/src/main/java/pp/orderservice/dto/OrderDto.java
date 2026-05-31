package pp.orderservice.dto;

import lombok.Builder;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.commonlib.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderDto(
        Long id,
        UUID userUuid,
        Long restaurantId,
        String address,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemDto> items,
        Instant createdAt
)
{}
