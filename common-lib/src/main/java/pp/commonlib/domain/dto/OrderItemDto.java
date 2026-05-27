package pp.commonlib.domain.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemDto(
        Long itemId,
        String itemNameSnapshot,
        BigDecimal itemPriceSnapshot,
        Integer quantity
)
{}
