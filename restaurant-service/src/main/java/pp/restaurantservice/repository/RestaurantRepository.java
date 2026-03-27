package pp.restaurantservice.repository;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.Restaurant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantRepository extends R2dbcRepository<Restaurant, Long> {

    @Query(
        """
        SELECT *
        FROM restaurants r
        JOIN restaurant_brands rb ON r.brand_id = rb.id
        WHERE rb.id = :brandId
        """
    )
    Flux<Restaurant> findAllByBrandId(Long brandId);

    @Query(
        """
        SELECT *
        FROM restaurants r
        JOIN restaurant_brands rb ON r.brand_id = rb.id
        LEFT JOIN restaurant_addresses ra on ra.restaurant_id = r.id
        WHERE ra.id IS NULL AND r.status != 'NON_ACTIVE' AND rb.id = :brandId
        """
    )
    Flux<Restaurant> findAllWithoutAddress(Long brandId);

    @Query(
        """
        SELECT *
        FROM restaurants r
        WHERE r.id = :restaurantId
        AND r.status != 'NON_ACTIVE'
        AND NOT EXISTS (
            SELECT 1
            FROM restaurant_addresses ra
            WHERE ra.restaurant_id = r.id
        )
        """
    )
    Flux<Restaurant> findAllWithoutAddressByRestaurantId(Long restaurantId);
}
