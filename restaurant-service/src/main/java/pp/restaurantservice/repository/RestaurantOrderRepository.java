package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantOrder;

@Repository
public interface RestaurantOrderRepository extends R2dbcRepository<RestaurantOrder, Long> {
}
