package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.*;
import pp.restaurantservice.service.RestaurantStaffService;
import reactor.core.publisher.Mono;

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
    public Mono<RestaurantStaffDto> createStaff(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody RestaurantStaffCreateRequest request) {
        var currentUserId = jwt.getSubject();
        return restaurantStaffService.createStaff(currentUserId, request);
    }

    @PostMapping("/manager")
    public Mono<RestaurantStaffDto> createManager(@AuthenticationPrincipal Jwt jwt,
                                                  @RequestBody RestaurantManagerCreateRequest request) {
        var currentUserId = jwt.getSubject();
        return restaurantStaffService.createManager(currentUserId, request);
    }

    @PatchMapping("/{staffId}/status")
    public Mono<RestaurantStaffDto> changeStatus(@PathVariable("staffId") String staffId,
                                                 @AuthenticationPrincipal Jwt jwt,
                                                 @RequestBody RestaurantStaffChangeStatusRequest request) {
        var currentUserId = jwt.getSubject();
        return restaurantStaffService.changeStaffStatus(staffId, currentUserId, request.getStatus());
    }

    @PatchMapping("/{staffId}/role")
    public Mono<RestaurantStaffDto> changeRole(@PathVariable("staffId") String staffId,
                                               @AuthenticationPrincipal Jwt jwt,
                                               @RequestBody RestaurantStaffChangeRoleRequest request) {
        var currentUserId = jwt.getSubject();
        return restaurantStaffService.changeStaffRole(staffId, currentUserId, request.getRole());
    }
}
