package pp.orderservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.commonlib.domain.enums.OrderStatus;
import pp.commonlib.domain.event.OrderPaidEvent;
import pp.orderservice.dto.OrderCreateRequest;
import pp.orderservice.dto.OrderCreateResponse;
import pp.orderservice.dto.OrderItemCreateResponse;
import pp.orderservice.entity.Order;
import pp.orderservice.entity.OrderItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class OrderUtils {

    public Order buildOrder(UUID userUuid, OrderCreateRequest request) {
        return Order.builder()
                .userUuid(userUuid)
                .restaurantId(request.restaurantId())
                .address(request.address())
                .status(OrderStatus.PENDING_PAYMENT)
                .totalAmount(request.totalAmount())
                .createdAt(Instant.now())
                .build();
    }

    public OrderCreateResponse buildOrderCreateResponse(Order order, List<OrderItemCreateResponse> items) {
        return OrderCreateResponse.builder()
                .orderId(order.getId())
                .userUuid(order.getUserUuid())
                .restaurantId(order.getRestaurantId())
                .address(order.getAddress())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .createdAt(order.getCreatedAt())
                .build();
    }

    public OrderPaidEvent buildOrderPaidEvent(Order order, List<OrderItemDto> items) {
        return OrderPaidEvent.builder()
                .orderId(order.getId())
                .restaurantId(order.getRestaurantId())
                .userUuid(order.getUserUuid())
                .address(order.getAddress())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }
}
