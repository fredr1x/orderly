package pp.restaurantservice.dto;

import pp.restaurantservice.entity.enums.RestaurantStatus;

public record RestaurantStatusUpdateRequest(
    Long restaurantId,
    RestaurantStatus status
)
{}
