package pp.restaurantservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantRating;
import pp.restaurantservice.entity.enums.RatingStatus;
import pp.restaurantservice.repository.projection.RestaurantRatingAvg;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RestaurantRatingRepository extends R2dbcRepository<RestaurantRating, Long> {

    Mono<Boolean> existsByClientUserIdAndRestaurantIdAndStatusNot(UUID clientUserId, Long restaurantId, RatingStatus status);




    @Query("SELECT restaurant_id, AVG(rating) as avg_rating " +
           "FROM restaurant_ratings WHERE status = 'APPROVED' " +
           "GROUP BY restaurant_id")
    Flux<RestaurantRatingAvg> findAverageRatingsByRestaurant();

    Mono<Long> countByRestaurantId(Long restaurantId);

    Mono<Long> countByRestaurantIdAndStatus(Long restaurantId, RatingStatus status);

    Flux<RestaurantRating> findAllByRestaurantIdAndStatus(Long restaurantId, RatingStatus status, Pageable pageable);
}
