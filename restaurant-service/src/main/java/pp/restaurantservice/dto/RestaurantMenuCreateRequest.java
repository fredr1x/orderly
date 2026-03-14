package pp.restaurantservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import pp.restaurantservice.entity.enums.MenuType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenuCreateRequest {

    @NotNull(message = "restaurantId is required")
    private Long restaurantId;

    @NotNull
    private MenuType menuType;
}
