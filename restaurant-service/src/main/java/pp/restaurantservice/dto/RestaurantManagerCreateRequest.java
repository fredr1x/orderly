package pp.restaurantservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.restaurantservice.entity.enums.StaffRole;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantManagerCreateRequest extends RestaurantStaffCreateRequest {
    private static final StaffRole role = StaffRole.ROLE_RESTAURANT_MANAGER;

    public RestaurantManagerCreateRequest(Long restaurantId, String email,
                                          String phoneNumber, String password,
                                          String firstName, String lastName) {
        super(restaurantId, StaffRole.ROLE_RESTAURANT_MANAGER, email, phoneNumber, password, firstName, lastName);
    }

    @Override
    public StaffRole getRole() {
        return StaffRole.ROLE_RESTAURANT_MANAGER;
    }
}
