package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.*;
import pp.restaurantservice.entity.Restaurant;
import pp.restaurantservice.entity.enums.RestaurantStatus;
import pp.restaurantservice.mapper.RestaurantMapper;
import pp.restaurantservice.repository.RestaurantRepository;
import pp.restaurantservice.utils.JwtUtils;
import pp.restaurantservice.utils.RestaurantUtils;
import pp.restaurantservice.utils.RoleUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.fromString;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    private final RestaurantRepository restaurantRepository;

    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantBrandService restaurantBrandService;

    public Mono<Restaurant> findById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Restaurant not found")));
    }

    public Flux<RestaurantDto> findAllWithoutAddress(String currentUserId) {
        return JwtUtils.extractRoles()
                .flatMapMany(roles -> {
                    if (roles.contains(RoleUtils.RESTAURANT_OWNER)) {
                        return restaurantBrandService.findByOwnerUserId(fromString(currentUserId))
                                .flatMapMany(brand ->
                                        restaurantRepository.findAllWithoutAddress(brand.getId())
                                )
                                .map(restaurantMapper::toRestaurantDto);
                    }

                    if (roles.contains(RoleUtils.RESTAURANT_MANAGER)) {
                        return restaurantStaffService.findStaffByUserId(fromString(currentUserId))
                                .flatMapMany(staff ->
                                        restaurantRepository.findAllWithoutAddressByRestaurantId(staff.getRestaurantId())
                                )
                                .map(restaurantMapper::toRestaurantDto);
                    }

                    return Mono.error(() -> new RuntimeException("Not enough permissions"));
                });
    }

    public Mono<RestaurantDto> saveAndReturn(Restaurant restaurant) {
        return restaurantRepository.save(restaurant)
                .map(restaurantMapper::toRestaurantDto);
    }

    public Mono<RestaurantDto> createRestaurant(String currentUserId, RestaurantCreateRequest request) {
        return restaurantBrandService.validateRelatedBrand(request.getBrandId(), fromString(currentUserId))
                .then(restaurantRepository.save(RestaurantUtils.buildRestaurant(request))
                        .map(restaurantMapper::toRestaurantDto)
                );
    }

    public Flux<RestaurantDto> findAllByBrandId(String currentUserId, Long brandId) {
        return restaurantBrandService.findByOwnerUserId(fromString(currentUserId))
                .flatMapMany(brand ->
                        restaurantRepository.findAllByBrandId(brandId)
                                .map(restaurantMapper::toRestaurantDto)
                );
    }

    public Mono<RestaurantDto> updateRestaurant(String currentUserId, RestaurantUpdateRequest request) {
        return restaurantStaffService.validateStaffRoleAndSameRestaurant(fromString(currentUserId), request.getRestaurantId())
                .then(restaurantRepository.findById(request.getRestaurantId())
                        .flatMap(restaurant -> {
                            restaurantMapper.updateRestaurantFromRequest(request, restaurant);
                            return restaurantRepository.save(restaurant)
                                    .map(restaurantMapper::toRestaurantDto);
                        })
                );
    }

    public Mono<RestaurantDto> updateRestaurantStatus(String currentUserId, RestaurantStatusUpdateRequest request) {
        return restaurantStaffService.validateStaffRoleAndSameRestaurant(fromString(currentUserId), request.getRestaurantId())
                .then(restaurantRepository.findById(request.getRestaurantId())
                        .flatMap(restaurant -> {
                            restaurant.setStatus(request.getStatus());
                            return restaurantRepository.save(restaurant)
                                    .map(restaurantMapper::toRestaurantDto);
                        })
                );
    }

    public Mono<Void> deleteRestaurant(String currentUserId, Long restaurantId) {
        return restaurantStaffService.validateStaffRoleAndSameRestaurant(fromString(currentUserId), restaurantId)
                .then(restaurantRepository.findById(restaurantId).
                        flatMap(restaurant -> {
                            restaurant.setStatus(RestaurantStatus.NON_ACTIVE);
                            return restaurantRepository.save(restaurant);
                        })
                )
                .then();
    }

    public Mono<Void> validateStaffAndRestaurant(UUID currentUserId, Long restaurantId) {
        return restaurantStaffService.findStaffByUserId(currentUserId)
                .flatMap(staff -> findById(restaurantId)
                        .flatMap(restaurant -> {
                            if (!Objects.equals(staff.getRestaurantId(), restaurant.getId())) {
                                return Mono.error(new RuntimeException("You do not have access to this restaurant"));
                            }
                            return Mono.empty();
                        }));
    }
}
