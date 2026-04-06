package pp.restaurantservice.dto;

import pp.restaurantservice.entity.enums.StaffRole;

public record RestaurantStaffCreateRequest(
    Long restaurantId,
    StaffRole role,
    String email,
    String phoneNumber,
    String password,
    String firstName,
    String lastName
)
{}
