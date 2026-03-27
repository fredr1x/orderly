package pp.restaurantservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pp.restaurantservice.entity.RestaurantAddress;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface RestaurantAddressRepository extends R2dbcRepository<RestaurantAddress, Long> {
    Mono<Boolean> existsByRestaurantId(Long restaurantId);

    @Query(
        """
        SELECT *
        FROM restaurant_addresses ra
        JOIN restaurants r ON ra.restaurant_id = r.id
        JOIN public.restaurant_brands rb on r.brand_id = rb.id
        WHERE r.status != 'NON_ACTIVE' AND rb.id = :brandId
        """
    )
    Flux<RestaurantAddress> findAllByBrandId(@Param("brandId") Long brandId);

    Mono<RestaurantAddress> findByRestaurantId(Long restaurantId);

    @Query(
        """
        INSERT INTO restaurant_addresses(restaurant_id, formatted, location, country, city, street, house, floor, comment, created_at)
        VALUES (:restaurantId, :formatted, ST_GeomFromEWKB(decode(:location, 'hex'))::geography,
                :country, :city, :street, :house, :floor, :comment, :createdAt)
        RETURNING *
        """
    )
    Mono<RestaurantAddress> save(@Param("restaurantId") Long restaurantId,
                                 @Param("formatted") String formatted,
                                 @Param("location") String location,
                                 @Param("country") String country,
                                 @Param("city") String city,
                                 @Param("street") String street,
                                 @Param("house") String house,
                                 @Param("floor") String floor,
                                 @Param("comment") String comment,
                                 @Param("createdAt") Instant createdAt
    );

    @Query(
        """
        UPDATE restaurant_addresses
        SET formatted = :formatted,
            location = ST_GeomFromEWKB(decode(:location, 'hex'))::geography,
            country = :country,
            city = :city,
            street = :street,
            house = :house,
            floor = :floor,
            comment = :comment,
            updated_at = :updatedAt
        WHERE id = :id
        RETURNING *
        """
    )
    Mono<RestaurantAddress> update(@Param("id") Long id,
                                   @Param("formatted") String formatted,
                                   @Param("location") String location,
                                   @Param("country") String country,
                                   @Param("city") String city,
                                   @Param("street") String street,
                                   @Param("house") String house,
                                   @Param("floor") String floor,
                                   @Param("comment") String comment,
                                   @Param("updatedAt") Instant updatedAt
    );
}
