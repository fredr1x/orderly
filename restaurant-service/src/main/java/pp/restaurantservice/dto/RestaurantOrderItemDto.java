package pp.restaurantservice.dto;

import lombok.Builder;

@Builder
public record RestaurantOrderItemDto(
    Long itemId,
    String itemName,
    Integer quantity
)
{}
