package pp.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.restaurantservice.entity.enums.StaffRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantStaffCreateRequest {
    private Long restaurantId;
    private StaffRole role;
    private String email;
    private String phoneNumber;
    private String password;
    private String firstName;
    private String lastName;
}
