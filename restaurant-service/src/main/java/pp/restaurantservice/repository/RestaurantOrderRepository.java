package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantOrder;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantOrderRepository extends R2dbcRepository<RestaurantOrder, Long> {
    Flux<RestaurantOrder> findAllByRestaurantIdAndStatus(Long restaurantId, RestaurantOrderStatus status);
}
