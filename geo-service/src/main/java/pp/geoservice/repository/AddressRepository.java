package pp.geoservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pp.geoservice.entity.Address;
import reactor.core.publisher.Mono;

@Repository
public interface AddressRepository extends R2dbcRepository<Address, Long> {

    @Query("""
        SELECT *
        FROM addresses
        WHERE ST_DWithin(
            location,
            ST_MakePoint(:longitude, :latitude)::geography,
            :radius
        )
        ORDER BY location <-> ST_MakePoint(:longitude, :latitude)::geography
        LIMIT 1;
        """)
    Mono<Address> findByPointAndRadius(@Param("longitude") double longitude,
                                       @Param("latitude") double latitude,
                                       @Param("radius") int radius);


    @Query("""
        SELECT * FROM addresses
        WHERE ST_DWithin(
            location,
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
            30
        )
        ORDER BY location <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
        LIMIT 1
    """)
    Mono<Address> findNearest(@Param("longitude") double longitude,
                              @Param("latitude") double latitude);

    @Query("""
        INSERT INTO addresses (formatted, location, country, province, locality, street, house)
        VALUES (:formatted, ST_GeomFromEWKB(decode(:location, 'hex'))::geography,
                :country, :province, :locality, :street, :house)
        RETURNING *
        """)
    Mono<Address> saveAddressAndReturn(@Param("formatted") String formatted,
                                       @Param("location") String location,
                                       @Param("country") String country,
                                       @Param("province") String province,
                                       @Param("locality") String locality,
                                       @Param("street") String street,
                                       @Param("house") String house);

    @Query(
        """
        SELECT * FROM addresses a
        WHERE a.formatted = :formatted
        """)
    Mono<Address> findByFormatted(@Param("formatted") String formatted);
}
