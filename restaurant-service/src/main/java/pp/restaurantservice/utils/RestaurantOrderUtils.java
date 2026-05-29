package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.enums.RestaurantDecision;
import pp.commonlib.domain.event.OrderPaidEvent;
import pp.commonlib.domain.event.RestaurantDecisionEvent;
import pp.restaurantservice.dto.RestaurantOrderDto;
import pp.restaurantservice.dto.RestaurantOrderItemDto;
import pp.restaurantservice.entity.RestaurantOrder;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;

import java.time.Instant;
import java.util.List;

@UtilityClass
public class RestaurantOrderUtils {

    public RestaurantOrder buildRestaurantOrder(OrderPaidEvent event) {
        return RestaurantOrder.builder()
                .orderId(event.orderId())
                .restaurantId(event.restaurantId())
                .userUuid(event.userUuid())
                .status(RestaurantOrderStatus.PENDING)
                .createdAt(Instant.now())
                .build();
    }

    public RestaurantOrderDto buildRestaurantOrderDto(RestaurantOrder restaurantOrder, List<RestaurantOrderItemDto> items) {
        return RestaurantOrderDto.builder()
                .id(restaurantOrder.getId())
                .orderId(restaurantOrder.getOrderId())
                .restaurantId(restaurantOrder.getRestaurantId())
                .userUuid(restaurantOrder.getUserUuid())
                .status(restaurantOrder.getStatus())
                .createdAt(restaurantOrder.getCreatedAt())
                .modifiedBy(restaurantOrder.getModified_by())
                .modifiedAt(restaurantOrder.getModified_at())
                .items(items)
                .build();
    }

    public static RestaurantDecisionEvent buildRestaurantDecisionEvent(Long orderId, RestaurantDecision restaurantDecision) {
        return RestaurantDecisionEvent.builder()
                .orderId(orderId)
                .decision(restaurantDecision)
                .reason(restaurantDecision == RestaurantDecision.REJECTED
                        ? "Default reason" // todo get reason from endpoint
                        : ""
                )
                .build();
    }
}
