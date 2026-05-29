package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.restaurantservice.dto.RestaurantOrderDto;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;
import pp.restaurantservice.service.RestaurantOrderService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class RestaurantOrderController {

    private final RestaurantOrderService restaurantOrderService;

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
