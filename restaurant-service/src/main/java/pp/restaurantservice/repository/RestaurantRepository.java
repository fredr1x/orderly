package pp.restaurantservice.repository;

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
        """)
    Flux<Restaurant> findAllByBrandId(Long brandId);
}
