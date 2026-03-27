package pp.restaurantservice.dto;

import lombok.*;
import pp.restaurantservice.entity.enums.RatingStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRatingDto {
    private Long id;
    private UUID clientUserId;
    private Long restaurantId;
    private short rating;
    private String comment;
    private RatingStatus status;
    private Instant createdAt;
}
