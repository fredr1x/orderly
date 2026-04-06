package pp.restaurantservice.dto;

import pp.restaurantservice.entity.enums.StaffRole;

public record RestaurantManagerCreateRequest(
        Long restaurantId,
        String email,
        String phoneNumber,
        String password,
        String firstName,
        String lastName
) {
    public StaffRole role() {
        return StaffRole.ROLE_RESTAURANT_MANAGER;
    }
}
