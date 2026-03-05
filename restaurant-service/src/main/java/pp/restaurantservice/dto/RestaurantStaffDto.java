package pp.restaurantservice.dto;

import lombok.*;
import pp.restaurantservice.entity.enums.StaffRole;
import pp.restaurantservice.entity.enums.StaffStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantStaffDto {
    private Long id;
    private String userId;
    private Long restaurantId;
    private StaffRole role;
    private StaffStatus status;
    private Instant hiredAt;
    private Instant firedAt;
}
