package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.RestaurantCreateRequest;
import pp.restaurantservice.dto.RestaurantDto;
import pp.restaurantservice.dto.RestaurantStatusUpdateRequest;
import pp.restaurantservice.dto.RestaurantUpdateRequest;
import pp.restaurantservice.service.RestaurantService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pp.restaurantservice.utils.JwtUtils.extractSubject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{brandId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public Flux<RestaurantDto> getAllByBrandId(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long brandId) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.findAllByBrandId(currentUserId, brandId);
    }

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public Mono<RestaurantDto> createRestaurant(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody RestaurantCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.createRestaurant(currentUserId, request);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'RESTAURANT_MANAGER')")
    public Mono<RestaurantDto> updateRestaurant(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody RestaurantUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.updateRestaurant(currentUserId, request);
    }

    @PatchMapping("/status")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'RESTAURANT_MANAGER')")
    public Mono<RestaurantDto> updateRestaurantStatus(@AuthenticationPrincipal Jwt jwt,
                                                      @RequestBody RestaurantStatusUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.updateRestaurantStatus(currentUserId, request);
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'RESTAURANT_MANAGER')")
    public Mono<Void> deleteRestaurant(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable Long restaurantId) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.deleteRestaurant(currentUserId, restaurantId);
    }
}
