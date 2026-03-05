package pp.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.restaurantservice.entity.enums.RestaurantStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantStatusUpdateRequest {
    private Long restaurantId;
    private RestaurantStatus status;
}
