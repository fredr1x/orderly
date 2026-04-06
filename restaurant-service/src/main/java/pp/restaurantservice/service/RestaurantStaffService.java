package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pp.restaurantservice.constants.RoleConstants;
import pp.restaurantservice.dto.RestaurantStaffCreateRequest;
import pp.restaurantservice.dto.RestaurantStaffDto;
import pp.restaurantservice.entity.RestaurantStaff;
import pp.restaurantservice.entity.enums.StaffRole;
import pp.restaurantservice.entity.enums.StaffStatus;
import pp.restaurantservice.mapper.RestaurantStaffMapper;
import pp.restaurantservice.repository.RestaurantStaffRepository;
import pp.restaurantservice.service.client.UserServiceClient;
import pp.restaurantservice.utils.JwtUtils;
import pp.restaurantservice.utils.StaffUtils;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RestaurantStaffService {

    private final UserServiceClient userServiceClient;

    private final RestaurantStaffMapper restaurantStaffMapper;

    private final RestaurantStaffRepository restaurantStaffRepository;

    private final RestaurantBrandService restaurantBrandService;

    public Mono<RestaurantStaff> findStaffByUserId(UUID userId) {
        return restaurantStaffRepository
                .findByUserId(userId)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Staff with userId " + userId + " not found")));
    }

    public Mono<RestaurantStaffDto> findByUserId(String userId) {
        return restaurantStaffRepository
                .findByUserId(UUID.fromString(userId))
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Staff with userId " + userId + " not found")))
                .map(restaurantStaffMapper::toRestaurantStaffDto);
    }

    public Mono<Page<RestaurantStaffDto>> findAllByRestaurant(
            Long restaurantId,
            int page,
            int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return restaurantStaffRepository
                .findAllByRestaurant(
                        restaurantId,
                        pageRequest.getPageSize(),
                        pageRequest.getOffset()
                )
                .map(restaurantStaffMapper::toRestaurantStaffDto)
                .collectList()
                .zipWith(restaurantStaffRepository.countStaff(restaurantId))
                .map(tuple -> new PageImpl<>(
                        tuple.getT1(),
                        pageRequest,
                        tuple.getT2()
                ));
    }

    public Mono<RestaurantStaffDto> createStaff(String currentUserId,
                                                RestaurantStaffCreateRequest request) {

        return validateForManagerRole(UUID.fromString(currentUserId))
                .then(userServiceClient.createUser(StaffUtils.buildRegisterUserRequest(request))
                        .flatMap(user -> {
                            var staff = StaffUtils.buildRestaurantStaff(user.getKeycloakId(), request);
                            return restaurantStaffRepository.save(staff);
                        })
                        .map(restaurantStaffMapper::toRestaurantStaffDto));
    }

    public Mono<RestaurantStaffDto> createManager(String currentUserId, RestaurantStaffCreateRequest request) {
        var currentUserUUID = UUID.fromString(currentUserId);
        return validateStaffRoleAndSameRestaurant(currentUserUUID, request.restaurantId())
                .then(userServiceClient.createManager(StaffUtils.buildRegisterUserRequest(request)))
                .flatMap(user -> {
                    if (request.role() != StaffRole.ROLE_RESTAURANT_MANAGER)
                        return Mono.error(() -> new RuntimeException("Role mismatch to create manager"));

                    var staff = StaffUtils.buildRestaurantStaff(user.getKeycloakId(), request);
                    return restaurantStaffRepository.save(staff)
                            .map(restaurantStaffMapper::toRestaurantStaffDto);
                });
    }

    public Mono<RestaurantStaffDto> changeStaffStatus(
            String targetUserId,
            String currentUserId,
            StaffStatus newStatus
    ) {
        UUID currentUserUUID = UUID.fromString(currentUserId);
        UUID targetStaffUUID = UUID.fromString(targetUserId);

        return validateChangeRequest(currentUserUUID, targetStaffUUID)
                .flatMap(staff -> updateStaffStatus(staff, newStatus))
                .map(restaurantStaffMapper::toRestaurantStaffDto);
    }

    public Mono<RestaurantStaffDto> changeStaffRole(
            String targetUserId,
            String currentUserId,
            StaffRole newRole
    ) {
        UUID currentUserUUID = UUID.fromString(currentUserId);
        UUID targetStaffUUID = UUID.fromString(targetUserId);
        return validateChangeRequest(currentUserUUID, targetStaffUUID)
                .flatMap(staff -> updateStaffRole(staff, newRole))
                .map(restaurantStaffMapper::toRestaurantStaffDto);
    }

    public Mono<Void> validateManager(String currentUserId, Long restaurantId) {
        return findStaffByUserId(UUID.fromString(currentUserId))
                .flatMap(staff -> {
                    if (!staff.getRestaurantId().equals(restaurantId)) {
                        return Mono.error(() -> new RuntimeException("You do not manage this restaurant"));
                    }

                    if (staff.getRole() != StaffRole.ROLE_RESTAURANT_MANAGER) {
                        return Mono.error(() -> new RuntimeException("Not enough permission"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<RestaurantStaff> updateStaffRole(RestaurantStaff staff, StaffRole newRole) {
        staff.setRole(newRole);
        return restaurantStaffRepository.save(staff);
    }

    private Mono<Void> validateForManagerRole(UUID currentUserId) {
        return restaurantStaffRepository
                .findByUserId(currentUserId)
                .switchIfEmpty(Mono.error(new RuntimeException("Staff not found"))) // todo
                .then();
    }

    public Mono<Void> validateStaffRoleAndSameRestaurant(UUID currentUserId, Long restaurantId) {
        return JwtUtils.extractRoles()
                .flatMap(roles -> {
                    if (roles.contains(RoleConstants.RESTAURANT_OWNER)) {
                        return restaurantBrandService.findByOwnerUserId(currentUserId)
                                .flatMap(brand ->
                                        restaurantBrandService.validateRelatedRestaurant(brand.id(), restaurantId));
                    }

                    else if (roles.contains(RoleConstants.RESTAURANT_MANAGER)) {
                        return findStaffByUserId(currentUserId)
                                .flatMap(staff -> {
                                    if (staff.getRestaurantId().equals(restaurantId)) return Mono.empty();
                                    else return Mono.error(() -> new RuntimeException("You do not manage this restaurant"));
                                });
                    }

                    else return Mono.error(() -> new RuntimeException("Not enough permission"));
                })
                .then();
    }

    private Mono<RestaurantStaff> validateChangeRequest(UUID currentUserUuid, UUID targetStaffUuid) {
        return JwtUtils.extractRoles()
                .flatMap(roles -> restaurantStaffRepository.findByUserId(targetStaffUuid)
                        .flatMap(targetStaff -> {
                            if (roles.contains(RoleConstants.RESTAURANT_OWNER)) {
                                return restaurantBrandService.findByOwnerUserId(currentUserUuid)
                                        .flatMap(brand ->
                                                restaurantBrandService.validateRelatedRestaurant(brand.id(), targetStaff.getRestaurantId()))
                                        .thenReturn(targetStaff);
                            }

                            else if (roles.contains(RoleConstants.RESTAURANT_MANAGER)) {
                                return findStaffByUserId(currentUserUuid)
                                        .flatMap(manager -> {
                                            if (Objects.equals(manager.getRestaurantId(), targetStaff.getRestaurantId()))
                                                return Mono.just(targetStaff);
                                            else
                                                return Mono.error(new RuntimeException("You do not manage this staff"));
                                        });
                            }

                            else return Mono.error(new RuntimeException("Not enough permissions"));
                        })
                );
    }

    private Mono<RestaurantStaff> updateStaffStatus(RestaurantStaff staff, StaffStatus newStatus) {
        staff.setStatus(newStatus);
        if (newStatus == StaffStatus.TERMINATED) {
            staff.setFiredAt(Instant.now());
        } else if (newStatus == StaffStatus.ACTIVE) {
            staff.setHiredAt(Instant.now());
        }
        return restaurantStaffRepository.save(staff);
    }
}
