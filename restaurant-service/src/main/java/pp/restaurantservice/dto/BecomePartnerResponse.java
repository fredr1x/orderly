package pp.restaurantservice.dto;

import lombok.*;
import pp.restaurantservice.entity.enums.RestaurantStatus;

@Builder
public record BecomePartnerResponse(
    String ownerUserId,
    String brandName,
    String brandDescription,
    String email,
    String phoneNumber,
    RestaurantStatus restaurantStatus
)
{}
