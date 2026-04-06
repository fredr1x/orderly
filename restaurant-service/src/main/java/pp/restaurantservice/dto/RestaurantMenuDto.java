package pp.restaurantservice.dto;

import lombok.Builder;
import pp.restaurantservice.entity.enums.MenuType;

import java.time.LocalDateTime;

@Builder
public record RestaurantMenuDto(
    Long id,
    Long restaurantId,
    MenuType type,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
)
{}
