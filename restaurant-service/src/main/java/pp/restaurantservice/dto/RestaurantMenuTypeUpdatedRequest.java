package pp.restaurantservice.dto;

import jakarta.validation.constraints.NotNull;
import pp.restaurantservice.entity.enums.MenuType;


public record RestaurantMenuTypeUpdatedRequest(
    @NotNull Long menuId,
    @NotNull MenuType menuType
)
{}
