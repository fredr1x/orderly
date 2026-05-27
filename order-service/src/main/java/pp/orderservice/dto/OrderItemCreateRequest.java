package pp.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemCreateRequest(
        Long itemId,
        String itemNameSnapshot,
        BigDecimal itemPriceSnapshot,
        Integer quantity
)
{}
