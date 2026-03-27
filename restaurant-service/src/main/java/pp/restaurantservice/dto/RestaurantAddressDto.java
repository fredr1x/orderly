package pp.restaurantservice.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record RestaurantAddressDto(
        Long id,
        Long restaurantId,
        String formatted,
        String country,
        String city,
        String street,
        String house,
        String floor,
        String comment,
        Instant createdAt,
        Instant updatedAt,
        double longitude,
        double latitude
)
{}
