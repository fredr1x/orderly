package pp.restaurantservice.dto;

import jakarta.validation.constraints.NotNull;
import pp.restaurantservice.entity.enums.MenuType;

public record RestaurantMenuCreateRequest(

    @NotNull(message = "restaurantId is required")
    Long restaurantId,

    @NotNull
    MenuType menuType
)
{}
