package pp.restaurantservice.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBrandDto {
    private Long id;
    private String name;
    private String description;
    private UUID ownerUserId;
}
