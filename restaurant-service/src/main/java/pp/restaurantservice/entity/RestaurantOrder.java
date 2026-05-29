package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurant_orders")
public class RestaurantOrder {
    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("user_uuid")
    private UUID userUuid;

    @Column("status")
    private RestaurantOrderStatus status;

    @Column("created_at")
    private Instant createdAt;

    @Column("modified_by")
    private UUID modified_by;

    @Column("modified_at")
    private Instant modified_at;
}
