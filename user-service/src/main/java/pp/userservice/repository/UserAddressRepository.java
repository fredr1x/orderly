package pp.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pp.userservice.entity.UserAddress;

import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Query(value = """
    SELECT *
    FROM user_addresses a
    WHERE a.user_id = :userId and a.address_type = :addressType
      AND ST_DWithin(
            a.location,
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
            :radius
      )
    LIMIT 1
    """, nativeQuery = true)
    Optional<UserAddress> findExistingUserAddress(
            @Param("userId") Long userId,
            @Param("addressType") String addressType,
            @Param("longitude") double longitude,
            @Param("latitude") double latitude,
            @Param("radius") int radius
    );

    Optional<UserAddress> findByIdAndUser_Id(Long userAddressId, Long userId);
}
