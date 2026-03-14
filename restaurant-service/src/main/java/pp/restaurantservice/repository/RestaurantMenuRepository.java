package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantMenu;
import reactor.core.publisher.Flux;

@Repository
public interface RestaurantMenuRepository extends R2dbcRepository<RestaurantMenu, Long> {

    Flux<RestaurantMenu> findAllByRestaurantId(Long restaurantId);
}
