package pp.restaurantservice.dto;

import lombok.*;
import pp.restaurantservice.entity.enums.RestaurantStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BecomePartnerResponse {
    private String ownerUserId;
    private String brandName;
    private String brandDescription;
    private String email;
    private String phoneNumber;
    private RestaurantStatus restaurantStatus;
}
