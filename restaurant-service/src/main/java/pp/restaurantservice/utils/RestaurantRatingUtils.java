package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.RestaurantRatingCreateRequest;
import pp.restaurantservice.entity.RestaurantRating;
import pp.restaurantservice.entity.enums.RatingStatus;

import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class RestaurantRatingUtils {

    public RestaurantRating buildRestaurantRating(UUID clientUserId, Long restaurantId, RestaurantRatingCreateRequest request) {
        return RestaurantRating.builder()
                .clientUserId(clientUserId)
                .restaurantId(restaurantId)
                .rating(request.rating())
                .comment(request.comment())
                .status(RatingStatus.IN_REVIEW)
                .createdAt(Instant.now())
                .build();
    }
}
