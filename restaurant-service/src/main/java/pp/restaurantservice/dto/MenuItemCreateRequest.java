package pp.restaurantservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pp.restaurantservice.entity.enums.MenuItemType;

import java.math.BigDecimal;

public record MenuItemCreateRequest(

    @NotNull(message = "Menu id must be not null")
    Long menuId,

    @NotNull(message = "Menu item name must be not null")
    String name,

    String description,

    @NotNull(message = "Menu item type must be not null")
    MenuItemType type,

    @NotNull(message = "Menu item price must be not null")
    @Min(value = 0L, message = "Menu item price must be greater than zero")
    BigDecimal price,

    @NotNull(message = "Menu item weight must be not null")
    @Min(value = 0L, message = "Menu item weight must be greater than zero")
    BigDecimal weightGrams,

    @NotNull(message = "Menu item calories must be not null")
    @Min(value = 0L, message = "Menu item calories must be greater than zero")
    Integer calories,

    @NotNull(message = "Menu item preparation time must be not null")
    @Min(value = 0L, message = "Menu item preparation time must be greater than zero")
    Integer preparationTimeMinutes
)
{}
