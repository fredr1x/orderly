package pp.restaurantservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RestaurantRatingUpdateRequest(
        @NotNull(message = "Rating must be not null")
        @Min(value = 1, message = "Min rating must be 1")
        @Max(value = 5, message = "Max rating must be 5")
        Short rating,

        String comment
)
{}
