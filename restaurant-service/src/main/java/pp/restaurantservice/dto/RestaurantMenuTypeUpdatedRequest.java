package pp.restaurantservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.restaurantservice.entity.enums.MenuType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenuTypeUpdatedRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private MenuType menuType;
}
