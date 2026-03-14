package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.restaurantservice.entity.enums.MenuItemType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("menu_items")
public class MenuItem {

    @Id
    private Long id;

    @Column("menu_id")
    private Long menuId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("type")
    private MenuItemType type;

    @Column("price")
    private BigDecimal price;

    @Column("image_url")
    private String imageUrl;

    @Column("weight_grams")
    private BigDecimal weightGrams;

    @Column("calories")
    private Integer calories;

    @Column("preparation_time_minutes")
    private Integer preparationTimeMinutes;

    @Column("is_available")
    private boolean isAvailable = true;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
