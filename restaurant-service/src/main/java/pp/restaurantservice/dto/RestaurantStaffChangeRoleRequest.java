package pp.restaurantservice.dto;

import pp.restaurantservice.entity.enums.StaffRole;

public record RestaurantStaffChangeRoleRequest(
    StaffRole role
)
{}
