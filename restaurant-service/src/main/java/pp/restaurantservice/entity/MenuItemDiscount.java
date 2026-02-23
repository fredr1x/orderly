package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("menu_items_discount")
public class MenuItemDiscount {

    @Id
    private Long id;

    @Column("menu_item_id")
    private Long menuItemId;

    @Column("discount_percent")
    private Integer discountPercent;

    @Column("is_active")
    private Boolean isActive;

    @Column("start_time")
    private LocalDateTime startTime;

    @Column("end_time")
    private LocalDateTime endTime;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
