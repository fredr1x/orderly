package pp.restaurantservice.dto;

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
public class MenuItemUpdateRequest {
    private Long id;
    private String name;
    private String description;
    private MenuItemType type;
    private BigDecimal price;
    private BigDecimal weightGrams;
    private Integer calories;
    private Integer preparationTimeMinutes;
}
