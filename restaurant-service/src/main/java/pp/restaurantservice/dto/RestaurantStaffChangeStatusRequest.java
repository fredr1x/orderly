package pp.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.restaurantservice.entity.enums.StaffStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantStaffChangeStatusRequest {
    StaffStatus status;
}
