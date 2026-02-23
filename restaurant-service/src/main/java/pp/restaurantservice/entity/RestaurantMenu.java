package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.restaurantservice.entity.enums.MenuType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurant_menus")
public class RestaurantMenu {

    @Id
    private Long id;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("type")
    private MenuType type;

    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
