package pp.restaurantservice.dto;

import pp.restaurantservice.entity.enums.StaffStatus;

public record RestaurantStaffChangeStatusRequest(
    StaffStatus status
)
{}
