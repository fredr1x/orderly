package pp.restaurantservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import pp.restaurantservice.entity.enums.MenuItemType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
    private Long id;
    private Long menuId;
    private String name;
    private String description;
    private MenuItemType menuItemType;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal price;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal weightGrams;

    private Integer calories;
    private Integer preparationTimeMinutes;
    private boolean isAvailable;
}
