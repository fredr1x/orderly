package pp.restaurantservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import pp.restaurantservice.entity.enums.MenuItemType;

import java.math.BigDecimal;

@Builder
public record MenuItemDto(
    Long id,
    Long menuId,
    String name,
    String description,
    MenuItemType menuItemType,

    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal price,

    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal weightGrams,

    Integer calories,
    Integer preparationTimeMinutes,
    boolean isAvailable
)
{}
