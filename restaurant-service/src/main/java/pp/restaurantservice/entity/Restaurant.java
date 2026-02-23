package pp.restaurantservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.restaurantservice.entity.enums.RestaurantStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurants")
public class Restaurant {

    @Id
    private Long id;

    @Column("brand_id")
    private Long brandId;

    @Column("name")
    private String name;

    @Column("status")
    private RestaurantStatus status;

    @Column("phone_number")
    private String phoneNumber;

    @Column("email")
    private String email;

    @Column("instagram_profile_link")
    private String instagramProfileLink;

    @Column("average_rating")
    private BigDecimal averageRating;

    @Column("rating_count")
    private Integer ratingCount;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
