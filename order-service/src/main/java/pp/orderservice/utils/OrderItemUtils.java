package pp.orderservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.orderservice.dto.OrderItemCreateRequest;
import pp.orderservice.dto.OrderItemCreateResponse;
import pp.orderservice.entity.OrderItem;

@UtilityClass
public class OrderItemUtils {

    public OrderItem buildOrderItem(Long orderId, OrderItemCreateRequest request) {
        return OrderItem.builder()
                .orderId(orderId)
                .itemId(request.itemId())
                .itemNameSnapshot(request.itemNameSnapshot())
                .itemPriceSnapshot(request.itemPriceSnapshot())
                .quantity(request.quantity())
                .build();
    }

    public OrderItemCreateResponse buildOrderItemCreateResponse(OrderItem orderItem) {
        return OrderItemCreateResponse.builder()
                .itemId(orderItem.getItemId())
                .itemNameSnapshot(orderItem.getItemNameSnapshot())
                .itemPriceSnapshot(orderItem.getItemPriceSnapshot())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItemDto buildOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .itemId(orderItem.getItemId())
                .itemNameSnapshot(orderItem.getItemNameSnapshot())
                .itemPriceSnapshot(orderItem.getItemPriceSnapshot())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
