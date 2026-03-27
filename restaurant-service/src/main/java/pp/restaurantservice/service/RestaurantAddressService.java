package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.RestaurantAddressCreateRequest;
import pp.restaurantservice.dto.RestaurantAddressDto;
import pp.restaurantservice.dto.RestaurantAddressUpdateRequest;
import pp.restaurantservice.entity.RestaurantAddress;
import pp.restaurantservice.mapper.RestaurantAddressMapper;
import pp.restaurantservice.repository.RestaurantAddressRepository;
import pp.restaurantservice.utils.JwtUtils;
import pp.restaurantservice.utils.RoleUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static pp.restaurantservice.utils.GeoUtils.toHex;
import static pp.restaurantservice.utils.GeoUtils.toPoint;

@Service
@RequiredArgsConstructor
public class RestaurantAddressService {

    private final RestaurantAddressRepository restaurantAddressRepository;
    private final RestaurantAddressMapper restaurantAddressMapper;

    private final RestaurantService restaurantService;
    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantBrandService restaurantBrandService;

    public Mono<RestaurantAddress> findById(Long id) {
        return restaurantAddressRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Restaurant address not found")));
    }

    public Mono<RestaurantAddressDto> findDtoById(Long id) {
        return findById(id)
                .map(restaurantAddressMapper::toRestaurantAddressDto);
    }

    public Mono<RestaurantAddressDto> findByRestaurantId(Long restaurantId) {
        return restaurantService.findById(restaurantId)
                .flatMap(restaurant -> restaurantAddressRepository.findByRestaurantId(restaurant.getId())
                        .map(restaurantAddressMapper::toRestaurantAddressDto));
    }

    public Flux<RestaurantAddressDto> findAllByBrandId(Long brandId) {
        return restaurantBrandService.findById(brandId)
                .flatMapMany(brand -> restaurantAddressRepository.findAllByBrandId(brand.getId())
                        .map(restaurantAddressMapper::toRestaurantAddressDto)
                );
    }

    public Mono<RestaurantAddressDto> createAddress(String currentUserId, Long restaurantId, RestaurantAddressCreateRequest request) {
        return validateExistingAddress(restaurantId)
                .then(restaurantService.findById(restaurantId))
                .flatMap(restaurant ->
                        restaurantStaffService.validateStaffRoleAndSameRestaurant(UUID.fromString(currentUserId), restaurant.getId())
                                .thenReturn(restaurant)
                ).flatMap(restaurant ->
                        restaurantAddressRepository.save(
                                restaurantId,
                                request.formatted(),
                                toHex(toPoint(request.longitude(), request.latitude())),
                                request.country(),
                                request.city(),
                                request.street(),
                                request.house(),
                                request.floor(),
                                request.comment(),
                                Instant.now()
                        )
                ).map(restaurantAddressMapper::toRestaurantAddressDto);
    }

    public Mono<RestaurantAddressDto> updateAddress(String currentUserId,
                                                    Long addressId,
                                                    RestaurantAddressUpdateRequest request) {
        var currentUserUuid = UUID.fromString(currentUserId);
        return findById(addressId)
                .flatMap(address -> validateUpdatePermissions(currentUserUuid, address).thenReturn(address))
                .flatMap(address -> {
                    restaurantAddressMapper.updateRestaurantAddressFromRequest(request, address);

                    if (request.longitude() != null && request.latitude() != null) {
                        address.setLocation(toPoint(request.longitude(), request.latitude()));
                    }

                    address.setUpdatedAt(Instant.now());
                    return restaurantAddressRepository.update(
                            address.getId(),
                            address.getFormatted(),
                            toHex(address.getLocation()),
                            address.getCountry(),
                            address.getCity(),
                            address.getStreet(),
                            address.getHouse(),
                            address.getFloor(),
                            address.getComment(),
                            address.getUpdatedAt()
                    );
                })
                .map(restaurantAddressMapper::toRestaurantAddressDto);
    }

    private Mono<Void> validateUpdatePermissions(UUID currentUserId, RestaurantAddress address) {
        return JwtUtils.extractRoles()
                .flatMap(roles -> {
                    if (roles.contains(RoleUtils.RESTAURANT_OWNER)) {
                        return restaurantBrandService.findByOwnerUserId(currentUserId)
                                .flatMap(brand -> restaurantBrandService
                                        .validateRelatedRestaurant(brand.getId(), address.getRestaurantId()));
                    }

                    if (roles.contains(RoleUtils.RESTAURANT_MANAGER)) {
                        return restaurantStaffService.findStaffByUserId(currentUserId)
                                .flatMap(staff -> {
                                    if (Objects.equals(staff.getRestaurantId(), address.getRestaurantId())) {
                                        return Mono.empty();
                                    }

                                    return Mono.error(() -> new RuntimeException("You do not manage this restaurant"));
                                });
                    }

                    return Mono.error(() -> new RuntimeException("Not enough permission"));
                })
                .then();
    }

    private Mono<Void> validateExistingAddress(Long restaurantId) {
        return restaurantAddressRepository.existsByRestaurantId(restaurantId)
                .flatMap(exists -> {
                    if (exists) return Mono.error(() -> new RuntimeException("This restaurant already has address"));
                    return Mono.empty();
                });
    }
}
