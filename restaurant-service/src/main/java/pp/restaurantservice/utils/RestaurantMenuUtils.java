package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.RestaurantMenuCreateRequest;
import pp.restaurantservice.entity.RestaurantMenu;

import java.time.LocalDateTime;

@UtilityClass
public class RestaurantMenuUtils {

    public static RestaurantMenu buildRestaurant(RestaurantMenuCreateRequest request) {
        return RestaurantMenu.builder()
                .restaurantId(request.restaurantId())
                .type(request.menuType())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
