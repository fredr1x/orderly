package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.RestaurantCreateRequest;
import pp.restaurantservice.dto.RestaurantDto;
import pp.restaurantservice.dto.RestaurantStatusUpdateRequest;
import pp.restaurantservice.dto.RestaurantUpdateRequest;
import pp.restaurantservice.entity.Restaurant;
import pp.restaurantservice.entity.enums.RestaurantStatus;
import pp.restaurantservice.mapper.RestaurantMapper;
import pp.restaurantservice.repository.RestaurantRepository;
import pp.restaurantservice.utils.RestaurantUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    private final RestaurantRepository restaurantRepository;

    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantBrandService restaurantBrandService;

    public Mono<RestaurantDto> saveAndReturn(Restaurant restaurant) {
        return restaurantRepository.save(restaurant)
                .map(restaurantMapper::toRestaurantDto);
    }

    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public Mono<RestaurantDto> createRestaurant(String currentUserId, RestaurantCreateRequest request) {
        return restaurantBrandService.validateRelatedBrand(request.getBrandId(), UUID.fromString(currentUserId))
                .then(restaurantRepository.save(RestaurantUtils.buildRestaurant(request))
                        .map(restaurantMapper::toRestaurantDto)
                );
    }

    public Flux<RestaurantDto> findAllByBrandId(String currentUserId, Long brandId) {
        return restaurantBrandService.findByOwnerUserId(UUID.fromString(currentUserId))
                .flatMapMany(brand ->
                        restaurantRepository.findAllByBrandId(brandId)
                                .map(restaurantMapper::toRestaurantDto)
                );
    }

    public Mono<RestaurantDto> updateRestaurant(String currentUserId, RestaurantUpdateRequest request) {
        return restaurantStaffService.validateStaffRoleAndSameRestaurant(UUID.fromString(currentUserId), request.getRestaurantId())
                .then(restaurantRepository.findById(request.getRestaurantId())
                        .flatMap(restaurant -> {
                            restaurantMapper.updateRestaurantFromRequest(request, restaurant);
                            return restaurantRepository.save(restaurant)
                                    .map(restaurantMapper::toRestaurantDto);
                        })
                );
    }

    public Mono<RestaurantDto> updateRestaurantStatus(String currentUserId, RestaurantStatusUpdateRequest request) {
        return restaurantStaffService.validateStaffRoleAndSameRestaurant(UUID.fromString(currentUserId), request.getRestaurantId())
                .then(restaurantRepository.findById(request.getRestaurantId())
                        .flatMap(restaurant -> {
                            restaurant.setStatus(request.getStatus());
                            return restaurantRepository.save(restaurant)
                                    .map(restaurantMapper::toRestaurantDto);
                        })
                );
    }

    public Mono<Void> deleteRestaurant(String currentUserId, Long restaurantId) {
        return restaurantStaffService.validateStaffRoleAndSameRestaurant(UUID.fromString(currentUserId), restaurantId)
                .then(restaurantRepository.findById(restaurantId).
                        flatMap(restaurant -> {
                            restaurant.setStatus(RestaurantStatus.NON_ACTIVE);
                            return restaurantRepository.save(restaurant);
                        })
                )
                .then();
    }
}
