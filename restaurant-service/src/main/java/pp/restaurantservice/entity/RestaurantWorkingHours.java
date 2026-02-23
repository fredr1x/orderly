package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurant_working_hours")
public class RestaurantWorkingHours {

    @Id
    private Long id;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("day_of_week")
    private Integer dayOfWeek;

    @Column("open_time")
    private LocalTime openTime;

    @Column("close_time")
    private LocalTime closeTime;

    @Column("is_active")
    private boolean isActive = true;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
