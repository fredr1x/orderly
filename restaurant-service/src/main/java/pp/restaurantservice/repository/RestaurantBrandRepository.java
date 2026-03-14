package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantBrand;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RestaurantBrandRepository extends R2dbcRepository<RestaurantBrand, Long> {

    Mono<Boolean> existsByName(String name);

    @Query(
        """
        SELECT 1
        FROM restaurant_brands rb
        WHERE rb.id = :brandId AND rb.owner_user_id = :ownerUserId
        """)
    Mono<Integer> validatedRelationship(@Param("brandId") Long brandId,
                                        @Param("ownerUserId") UUID ownerUserId);

    @Query(
        """
        SELECT 1
        FROM restaurant_brands rb
        JOIN restaurants r ON r.brand_id = rb.id
        WHERE r.brand_id = :brandId AND r.id = :restaurantId
        """)
    Mono<Integer> validateRelatedRestaurant(@Param("brandId") Long brandId,
                                            @Param("restaurantId") Long restaurantId);

    @Query(
        """
        SELECT *
        FROM restaurant_brands rb
        WHERE rb.owner_user_id = :ownerUserId
        """)
    Mono<RestaurantBrand> findByOwnerUserId(@Param("ownerUserId") UUID ownerUserId);
}
