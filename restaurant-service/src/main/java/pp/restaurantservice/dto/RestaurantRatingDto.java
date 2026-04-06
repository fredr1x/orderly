package pp.restaurantservice.dto;

import lombok.Builder;
import pp.restaurantservice.entity.enums.RatingStatus;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RestaurantRatingDto(
    Long id,
    UUID clientUserId,
    Long restaurantId,
    short rating,
    String comment,
    RatingStatus status,
    Instant createdAt
)
{}
