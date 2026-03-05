package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{brandId}")
    public Flux<RestaurantDto> getAllByBrandId(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long brandId) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.findAllByBrandId(currentUserId, brandId);
    }

    @PostMapping
    public Mono<RestaurantDto> createRestaurant(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody RestaurantCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.createRestaurant(currentUserId, request);
    }

    @PatchMapping
    public Mono<RestaurantDto> updateRestaurant(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody RestaurantUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.updateRestaurant(currentUserId, request);
    }

    @PatchMapping("/status")
    public Mono<RestaurantDto> updateRestaurantStatus(@AuthenticationPrincipal Jwt jwt,
                                                      @RequestBody RestaurantStatusUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.updateRestaurantStatus(currentUserId, request);
    }

    @DeleteMapping("/{restaurantId}")
    public Mono<Void> deleteRestaurant(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable Long restaurantId) {
        var currentUserId = extractSubject(jwt);
        return restaurantService.deleteRestaurant(currentUserId, restaurantId);
    }

    private static String extractSubject(Jwt jwt) {
        return jwt.getSubject();
    }
}
