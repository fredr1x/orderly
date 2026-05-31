package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.RestaurantOrderDto;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;
import pp.restaurantservice.service.RestaurantOrderService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class RestaurantOrderController {

    private final RestaurantOrderService restaurantOrderService;

    @GetMapping("/restaurants/{restaurantId}")
    public Flux<RestaurantOrderDto> getAllRestaurantOrdersByStatus(@AuthenticationPrincipal  Jwt jwt,
                                                                @PathVariable Long restaurantId,
                                                                @RequestParam RestaurantOrderStatus status) {
        return restaurantOrderService.getAllRestaurantOrdersByStatus(jwt.getSubject(), restaurantId, status);

    }

    @PatchMapping("/{orderId}/accept")
    @PreAuthorize("hasAnyRole('RESTAURANT_MANAGER', 'RESTAURANT_CHEF')")
    public Mono<RestaurantOrderDto> acceptRestaurantOrder(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable Long orderId) {

        return restaurantOrderService.updateRestaurantOrderStatus(
                jwt.getSubject(),
                orderId,
                RestaurantOrderStatus.APPROVED
        );
    }

    @PatchMapping("/{orderId}/reject")
    @PreAuthorize("hasAnyRole('RESTAURANT_MANAGER', 'RESTAURANT_CHEF')")
    public Mono<RestaurantOrderDto> rejectRestaurantOrder(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable Long orderId) {

        return restaurantOrderService.updateRestaurantOrderStatus(
                jwt.getSubject(),
                orderId,
                RestaurantOrderStatus.REJECTED
        );
    }
}
