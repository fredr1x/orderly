package pp.restaurantservice.dto;

import jakarta.validation.constraints.NotNull;
import pp.restaurantservice.entity.enums.RatingStatus;

public record RestaurantRatingStatusUpdateRequest(
        @NotNull Long restaurantId,
        @NotNull RatingStatus status
)
{}
