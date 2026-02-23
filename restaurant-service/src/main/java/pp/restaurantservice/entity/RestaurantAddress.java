package pp.restaurantservice.entity;

import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("restaurant_addresses")
public class RestaurantAddress {

    @Id
    private Long id;

    @Column("restaurant_id")
    private Long restaurantId;

    @Column("formatted")
    private String formatted;

    @Column("location")
    private Point location;

    @Column("country")
    private String country;

    @Column("city")
    private String city;

    @Column("street")
    private String street;

    @Column("house")
    private String house;

    @Column("floor")
    private String floor;

    @Column("comment")
    private String comment;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
