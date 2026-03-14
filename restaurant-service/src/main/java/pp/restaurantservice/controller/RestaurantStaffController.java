package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.*;
import pp.restaurantservice.service.RestaurantStaffService;
import reactor.core.publisher.Mono;

import static pp.restaurantservice.utils.JwtUtils.extractSubject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff")
public class RestaurantStaffController {

    private final RestaurantStaffService restaurantStaffService;

    @GetMapping
    public Mono<RestaurantStaffDto> findById(@AuthenticationPrincipal Jwt jwt) {
        return restaurantStaffService.findByUserId(jwt.getSubject());
    }

    @GetMapping("/all/{restaurantId}")
    public Mono<Page<RestaurantStaffDto>> findAll(@PathVariable Long restaurantId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return restaurantStaffService.findAllByRestaurant(restaurantId, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<RestaurantStaffDto> createStaff(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody RestaurantStaffCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantStaffService.createStaff(currentUserId, request);
    }

    @PostMapping("/manager")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public Mono<RestaurantStaffDto> createManager(@AuthenticationPrincipal Jwt jwt,
                                                  @RequestBody RestaurantManagerCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantStaffService.createManager(currentUserId, request);
    }

    @PatchMapping("/{staffId}/status")
    @PreAuthorize("hasAnyRole('RESTAURANT_MANAGER', 'RESTAURANT_OWNER')")
    public Mono<RestaurantStaffDto> changeStatus(@PathVariable String staffId,
                                                 @AuthenticationPrincipal Jwt jwt,
                                                 @RequestBody RestaurantStaffChangeStatusRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantStaffService.changeStaffStatus(staffId, currentUserId, request.getStatus());
    }

    @PatchMapping("/{staffId}/role")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<RestaurantStaffDto> changeRole(@PathVariable String staffId,
                                               @AuthenticationPrincipal Jwt jwt,
                                               @RequestBody RestaurantStaffChangeRoleRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantStaffService.changeStaffRole(staffId, currentUserId, request.getRole());
    }
}
