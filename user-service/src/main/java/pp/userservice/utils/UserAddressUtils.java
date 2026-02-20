package pp.userservice.utils;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import pp.userservice.dto.UserAddressCreateRequest;
import pp.userservice.entity.User;
import pp.userservice.entity.UserAddress;

import java.time.Instant;

@UtilityClass
public class UserAddressUtils {
    public static final int DEFAULT_SEARCH_RADIUS= 5;
    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), 4326);

    public static Point toPoint(double longitude, double latitude) {
        return GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }

    public static UserAddress buildUserAddress(User user, UserAddressCreateRequest request) {
        return UserAddress.builder()
                .user(user)
                .country(request.getCountry())
                .city(request.getCity())
                .street(request.getStreet())
                .house(request.getHouse())
                .apartment(request.getApartment())
                .floor(request.getFloor())
                .location(toPoint(request.getLongitude(), request.getLatitude()))
                .comment(request.getComment())
                .isDefault(true)
                .isActive(true)
                .addressType(request.getAddressType())
                .createdAt(Instant.now())
                .build();
    }
}
