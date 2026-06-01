package pp.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.orderservice.dto.OrderCreateRequest;
import pp.orderservice.dto.OrderCreateResponse;
import pp.orderservice.dto.OrderDto;
import pp.orderservice.service.OrderService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public Mono<OrderDto> getOrderById(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable Long id) {
        return orderService.getOrderById(jwt.getSubject(), id);
    }

    @PostMapping
    public Mono<OrderCreateResponse> createOrder(@AuthenticationPrincipal Jwt jwt,
                                                 @RequestBody OrderCreateRequest request) {
        return orderService.createOrder(jwt.getSubject(), request);
    }
}
