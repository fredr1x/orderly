package pp.restaurantservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantMenuChangeStatusRequest {

    @NotNull(message = "Menu id must be not null")
    @Min(value = 0, message = "Menu id must be greater than zero")
    private Long menuId;

    @NotNull(message = "Menu status must be not null")
    private boolean isActive;
}
