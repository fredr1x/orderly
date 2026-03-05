package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.restaurantservice.entity.enums.StaffRole;
import pp.restaurantservice.entity.enums.StaffStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_staff")
public class RestaurantStaff {

    @Id
    private Long id;

    @Column("user_id")
    private UUID userId;

    @Column("phone_number")
    private String phoneNumber;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("role")
    private StaffRole role;

    @Column("status")
    private StaffStatus status;

    @Column("hired_at")
    private Instant hiredAt;

    @Column("fired_at")
    private Instant firedAt;
}
