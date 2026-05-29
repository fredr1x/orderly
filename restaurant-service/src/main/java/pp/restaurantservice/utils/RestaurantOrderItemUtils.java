package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.restaurantservice.dto.RestaurantOrderItemDto;
import pp.restaurantservice.entity.RestaurantOrderItem;

@UtilityClass
public class RestaurantOrderItemUtils {

    public RestaurantOrderItem buildRestaurantOrderItem(Long restaurantOrderId, OrderItemDto orderItemDto) {
        return RestaurantOrderItem.builder()
                .restaurantOrderId(restaurantOrderId)
                .itemId(orderItemDto.itemId())
                .itemName(orderItemDto.itemNameSnapshot())
                .quantity(orderItemDto.quantity())
                .build();
    }

    public RestaurantOrderItemDto buildRestaurantOrderItemDto(RestaurantOrderItem restaurantOrderItem) {
        return RestaurantOrderItemDto.builder()
                .itemId(restaurantOrderItem.getItemId())
                .itemName(restaurantOrderItem.getItemName())
                .quantity(restaurantOrderItem.getQuantity())
                .build();
    }
}
