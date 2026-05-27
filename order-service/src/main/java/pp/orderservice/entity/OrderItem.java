package pp.orderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("order_items")
public class OrderItem {

    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("item_id")
    private Long itemId;

    @Column("item_name_snapshot")
    private String itemNameSnapshot;

    @Column("item_price_snapshot")
    private BigDecimal itemPriceSnapshot;

    @Column("quantity")
    private Integer quantity;
}
