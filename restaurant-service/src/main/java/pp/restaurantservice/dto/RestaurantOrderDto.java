package pp.restaurantservice.dto;

import lombok.Builder;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record RestaurantOrderDto(
        Long id,
        Long orderId,
        Long restaurantId,
        UUID userUuid,
        RestaurantOrderStatus status,
        Instant createdAt,
        UUID modifiedBy,
        Instant modifiedAt,
        List<RestaurantOrderItemDto> items
)
{}
