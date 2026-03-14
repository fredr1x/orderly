package pp.restaurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.RestaurantMenuChangeStatusRequest;
import pp.restaurantservice.dto.RestaurantMenuCreateRequest;
import pp.restaurantservice.dto.RestaurantMenuDto;
import pp.restaurantservice.dto.RestaurantMenuTypeUpdatedRequest;
import pp.restaurantservice.service.RestaurantMenuService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pp.restaurantservice.utils.JwtUtils.extractSubject;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/restaurant-menus")
public class RestaurantMenuController {

    private final RestaurantMenuService restaurantMenuService;

    @GetMapping("/{menuId}")
    public Mono<RestaurantMenuDto> getMenu(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable Long menuId) {
        var currentUserId = extractSubject(jwt);
        return restaurantMenuService.getById(currentUserId, menuId);
    }

    @GetMapping("/{restaurantId}")
    public Flux<RestaurantMenuDto> getAllMenus(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long restaurantId) {
        var currentUserId = extractSubject(jwt);
        return restaurantMenuService.getAllByRestaurantId(currentUserId, restaurantId);
    }

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<RestaurantMenuDto> createMenu(@AuthenticationPrincipal Jwt jwt,
                                              @Valid @RequestBody RestaurantMenuCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantMenuService.createMenu(currentUserId, request);
    }

    @PatchMapping("/type")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<RestaurantMenuDto> updateMenuType(@AuthenticationPrincipal Jwt jwt,
                                                  @Valid @RequestBody RestaurantMenuTypeUpdatedRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantMenuService.updateMenuType(currentUserId, request);
    }

    @PatchMapping("/status")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<RestaurantMenuDto> changeMenuStatus(@AuthenticationPrincipal Jwt jwt,
                                                    @Valid @RequestBody RestaurantMenuChangeStatusRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantMenuService.changeMenuStatus(currentUserId, request);
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMenu(@AuthenticationPrincipal Jwt jwt,
                                 @PathVariable Long menuId) {
        var currentUserId = extractSubject(jwt);
        return restaurantMenuService.deleteMenu(currentUserId, menuId);
    }
}
