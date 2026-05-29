package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantOrderItem;
import reactor.core.publisher.Flux;

@Repository
public interface RestaurantOrderItemRepository extends R2dbcRepository<RestaurantOrderItem, Long> {
    Flux<RestaurantOrderItem> findAllByRestaurantOrderId(Long restaurantOrderId);
}
