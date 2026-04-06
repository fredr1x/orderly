package pp.restaurantservice.dto;

import lombok.Builder;
import pp.restaurantservice.entity.enums.RestaurantStatus;

@Builder
public record RestaurantDto(
    Long id,
    Long brandId,
    String name,
    String email,
    String phoneNumber,
    RestaurantStatus status,
    String instagramProfileLink,
    Double averageRating,
    Integer ratingCount
)
{}
