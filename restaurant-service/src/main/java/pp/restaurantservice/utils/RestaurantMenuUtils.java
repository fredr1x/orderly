package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.RestaurantMenuCreateRequest;
import pp.restaurantservice.entity.RestaurantMenu;
import pp.restaurantservice.entity.enums.MenuType;

import java.time.LocalDateTime;

@UtilityClass
public class RestaurantMenuUtils {

    public static RestaurantMenu buildRestaurant(RestaurantMenuCreateRequest request) {
        return RestaurantMenu.builder()
                .restaurantId(request.getRestaurantId())
                .type(request.getMenuType())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
