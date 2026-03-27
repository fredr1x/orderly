package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.RestaurantAddressCreateRequest;
import pp.restaurantservice.entity.RestaurantAddress;

import java.time.Instant;

@UtilityClass
public class RestaurantAddressUtils {

    public RestaurantAddress buildRestaurantAddress(Long restaurantId, RestaurantAddressCreateRequest request) {
        var longitude = request.longitude();
        var latitude = request.latitude();
        return RestaurantAddress.builder()
                .restaurantId(restaurantId)
                .formatted(request.formatted())
                .location(GeoUtils.toPoint(longitude, latitude))
                .country(request.country())
                .city(request.city())
                .street(request.street())
                .house(request.house())
                .floor(request.floor())
                .comment(request.comment())
                .createdAt(Instant.now())
                .build();
    }
}
