package pp.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pp.userservice.entity.Role;
import pp.userservice.entity.enums.Value;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByValue(Value value);

    @Modifying
    @Query(nativeQuery = true, value =
    """
    INSERT INTO user_roles (user_id, role_id)
    VALUES (:userId, :roleId)
    """
    )
    void assignRole(@Param("userId") Long userId,
                    @Param("roleId") Long roleId);
}
