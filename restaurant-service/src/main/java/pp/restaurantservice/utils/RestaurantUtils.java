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
                .name(request.getBrandName())
                .status(RestaurantStatus.PENDING_APPROVAL)
                .email(request.getEmail())
                .instagramProfileLink(request.getImageProfileLink())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    public Restaurant buildRestaurant(RestaurantCreateRequest request) {
        return Restaurant.builder()
                .brandId(request.getBrandId())
                .name(request.getName())
                .status(RestaurantStatus.PENDING_APPROVAL)
                .email(request.getEmail())
                .instagramProfileLink(request.getInstagramProfileLink())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}
