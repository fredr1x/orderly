package pp.restaurantservice.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RestaurantBrandDto(
    Long id,
    String name,
    String description,
    UUID ownerUserId
)
{}
