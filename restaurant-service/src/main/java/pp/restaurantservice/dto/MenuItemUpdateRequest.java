package pp.restaurantservice.dto;

import pp.restaurantservice.entity.enums.MenuItemType;

import java.math.BigDecimal;

public record MenuItemUpdateRequest(
    Long id,
    String name,
    String description,
    MenuItemType type,
    BigDecimal price,
    BigDecimal weightGrams,
    Integer calories,
    Integer preparationTimeMinutes
)
{}
