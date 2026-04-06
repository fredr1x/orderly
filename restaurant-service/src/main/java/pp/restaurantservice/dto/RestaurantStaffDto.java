package pp.restaurantservice.dto;

import lombok.*;
import pp.restaurantservice.entity.enums.StaffRole;
import pp.restaurantservice.entity.enums.StaffStatus;

import java.time.Instant;

@Builder
public record RestaurantStaffDto(
    Long id,
    String userId,
    Long restaurantId,
    StaffRole role,
    StaffStatus status,
    Instant hiredAt,
    Instant firedAt
)
{}
