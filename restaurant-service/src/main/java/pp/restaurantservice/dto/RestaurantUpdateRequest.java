package pp.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateRequest {
    private Long restaurantId;
    private String name;
    private String email;
    private String phoneNumber;
    private String instagramProfileLink;
}
