package pp.restaurantservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RestaurantMenuChangeStatusRequest(

    @NotNull(message = "Menu id must be not null")
    @Min(value = 0, message = "Menu id must be greater than zero")
    Long menuId,

    @NotNull(message = "Menu status must be not null")
    boolean isActive
)
{}
