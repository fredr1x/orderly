package pp.restaurantservice.dto.context;

import lombok.*;
import pp.commonlib.domain.UserDto;
import pp.restaurantservice.dto.RestaurantBrandDto;
import pp.restaurantservice.dto.RestaurantDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerContext {
    private UserDto user;
    private RestaurantBrandDto brand;
    private RestaurantDto restaurant;
}
