package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.BecomePartnerResponse;
import pp.restaurantservice.dto.RestaurantBrandDto;
import pp.restaurantservice.entity.Restaurant;
import pp.restaurantservice.entity.RestaurantBrand;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class BrandUtils {

    public static RestaurantBrand buildRestaurantBrand(UUID ownerUserId, String name, String description) {
        return RestaurantBrand.builder()
                .name(name)
                .description(description)
                .ownerUserId(ownerUserId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BecomePartnerResponse buildBecomePartnerResponse(RestaurantBrandDto brand, Restaurant restaurant) {
        return BecomePartnerResponse.builder()
                .ownerUserId(brand.ownerUserId().toString())
                .brandName(brand.name())
                .brandDescription(brand.description())
                .email(restaurant.getEmail())
                .phoneNumber(restaurant.getPhoneNumber())
                .restaurantStatus(restaurant.getStatus())
                .build();
    }
}
