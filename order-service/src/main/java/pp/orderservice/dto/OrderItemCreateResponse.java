package pp.orderservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemCreateResponse (
        Long itemId,
        String itemNameSnapshot,
        BigDecimal itemPriceSnapshot,
        Integer quantity
)
{}
