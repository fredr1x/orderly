package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.BecomePartnerRequest;
import pp.restaurantservice.dto.RestaurantCreateRequest;
import pp.restaurantservice.entity.Restaurant;
import pp.restaurantservice.entity.enums.RestaurantStatus;

@UtilityClass
public class RestaurantUtils {

    public Restaurant buildRestaurant(Long brandId, BecomePartnerRequest request) {
        return Restaurant.builder()
                .brandId(brandId)
                .name(request.brandName())
                .status(RestaurantStatus.PENDING_APPROVAL)
                .email(request.email())
                .instagramProfileLink(request.imageProfileLink())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public Restaurant buildRestaurant(RestaurantCreateRequest request) {
        return Restaurant.builder()
                .brandId(request.brandId())
                .name(request.name())
                .status(RestaurantStatus.PENDING_APPROVAL)
                .email(request.email())
                .instagramProfileLink(request.instagramProfileLink())
                .phoneNumber(request.phoneNumber())
                .build();
    }
}
