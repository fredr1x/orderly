package pp.restaurantservice.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.restaurantservice.entity.enums.RatingStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurant_ratings")
public class RestaurantRating {

    @Id
    private Long id;

    @Column("client_user_id")
    private UUID clientUserId;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("rating")
    private Short rating;

    @Column("comment")
    private String comment;

    @Column("status")
    @Enumerated(EnumType.STRING)
    private RatingStatus status;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}
