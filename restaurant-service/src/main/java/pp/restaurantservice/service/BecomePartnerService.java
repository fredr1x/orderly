package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.UserDto;
import pp.restaurantservice.dto.BecomePartnerRequest;
import pp.restaurantservice.dto.BecomePartnerResponse;
import pp.restaurantservice.dto.RestaurantBrandDto;
import pp.restaurantservice.dto.context.PartnerContext;
import pp.restaurantservice.service.client.UserServiceClient;
import pp.restaurantservice.utils.BrandUtils;
import pp.restaurantservice.utils.RestaurantUtils;
import pp.restaurantservice.utils.StaffUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BecomePartnerService {

    private final UserServiceClient userServiceClient;
    private final RestaurantService restaurantService;
    private final RestaurantBrandService restaurantBrandService;

    public Mono<BecomePartnerResponse> becomePartner(BecomePartnerRequest request) {
        return userServiceClient.createOwner(StaffUtils.buildRegisterUserRequest(request))
                .flatMap(user ->
                        restaurantBrandService.createBrand(
                                user.getKeycloakId().toString(),
                                request.brandName(),
                                request.brandDescription()
                        ).map(brand -> buildPartnerContext(user, brand))
                )
                .flatMap(ctx -> {
                    var restaurant = RestaurantUtils.buildRestaurant(ctx.getBrand().id(), request);

                    return restaurantService.saveAndReturn(restaurant)
                            .map(r -> BrandUtils.buildBecomePartnerResponse(ctx.getBrand(), restaurant));
                });
    }

    private static PartnerContext buildPartnerContext(UserDto user, RestaurantBrandDto brand) {
        return PartnerContext.builder()
                .user(user)
                .brand(brand)
                .restaurant(null)
                .build();
    }
}
