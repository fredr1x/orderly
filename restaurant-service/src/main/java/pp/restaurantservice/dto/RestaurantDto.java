package pp.restaurantservice.dto;

import lombok.*;
import pp.restaurantservice.entity.enums.RestaurantStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private Long brandId;
    private String name;
    private String email;
    private String phoneNumber;
    private RestaurantStatus status;
    private String instagramProfileLink;
    private Double averageRating;
    private Integer ratingCount;
}
