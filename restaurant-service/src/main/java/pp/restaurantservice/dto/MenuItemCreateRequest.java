package pp.restaurantservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.restaurantservice.entity.enums.MenuItemType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemCreateRequest {

    @NotNull(message = "Menu id must be not null")
    private Long menuId;

    @NotNull(message = "Menu item name must be not null")
    private String name;

    private String description;

    @NotNull(message = "Menu item type must be not null")
    private MenuItemType type;

    @NotNull(message = "Menu item price must be not null")
    @Min(value = 0L, message = "Menu item price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Menu item weight must be not null")
    @Min(value = 0L, message = "Menu item weight must be greater than zero")
    private BigDecimal weightGrams;

    @NotNull(message = "Menu item calories must be not null")
    @Min(value = 0L, message = "Menu item calories must be greater than zero")
    private Integer calories;

    @NotNull(message = "Menu item preparation time must be not null")
    @Min(value = 0L, message = "Menu item preparation time must be greater than zero")
    private Integer preparationTimeMinutes;
}
