package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RestaurantStaffRepository extends R2dbcRepository<RestaurantStaff, Long> {

    @Query(
       """
       SELECT id, user_id, restaurant_id, role, status, hired_at, fired_at
       FROM restaurant_staff AS rs
       WHERE rs.user_id = :userId
       """)
    Mono<RestaurantStaff> findByUserId(UUID userId);

    @Query(
        """
        SELECT id, user_id, restaurant_id, role, status, hired_at, fired_at
        FROM restaurant_staff AS rs
        WHERE rs.user_id = :userId AND rs.restaurant_id = :restaurantId
        """)
    Mono<RestaurantStaff> findByUserIdAndRestaurantId(UUID userId, Long restaurantId);

    @Query(
        """
        SELECT id, user_id, restaurant_id, role, status, hired_at, fired_at
        FROM restaurant_staff
        WHERE restaurant_id = :restaurantId
        ORDER BY id
        LIMIT :limit OFFSET :offset
        """)
    Flux<RestaurantStaff> findAllByRestaurant(
            Long restaurantId,
            int limit,
            long offset
    );

    @Query(
        """
        SELECT COUNT(*)
        FROM restaurant_staff AS rf
        WHERE rf.restaurant_id = :restaurantId
        """)
    Mono<Long> countStaff(Long restaurantId);
}
