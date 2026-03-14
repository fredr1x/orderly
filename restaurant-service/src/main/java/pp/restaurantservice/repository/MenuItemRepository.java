package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.MenuItem;
import reactor.core.publisher.Flux;

@Repository
public interface MenuItemRepository extends R2dbcRepository<MenuItem, Long> {
    Flux<MenuItem> findAllByMenuId(Long menuId);
}
