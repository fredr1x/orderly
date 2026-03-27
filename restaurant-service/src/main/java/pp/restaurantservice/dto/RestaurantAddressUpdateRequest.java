package pp.restaurantservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RestaurantAddressUpdateRequest(
        String formatted,
        String country,
        String city,
        String street,
        String house,
        String floor,
        String comment,

        @Min(-180)
        @Max(180)
        Double longitude,

        @Min(-90)
        @Max(90)
        Double latitude
)
{}
