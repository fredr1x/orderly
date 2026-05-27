package pp.orderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.commonlib.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {

    @Id
    private Long id;

    @Column("user_uuid")
    private UUID userUuid;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("address")
    private String address;

    @Column("status")
    private OrderStatus status;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("created_at")
    private Instant createdAt;
}
