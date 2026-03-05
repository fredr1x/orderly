package pp.restaurantservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreateRequest {
    private Long brandId;
    private String name;
    private String phoneNumber;
    private String email;
    private String instagramProfileLink;
}
