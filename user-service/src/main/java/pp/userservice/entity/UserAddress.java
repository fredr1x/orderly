package pp.userservice.entity;

import lombok.*;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import pp.userservice.entity.enums.AddressType;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Table(name = "user_addresses")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house")
    private String house;

    @Column(name = "apartment")
    private String apartment;

    @Column(name = "floor")
    private String floor;

    @Column(
            name = "location",
            columnDefinition = "geography(Point, 4326)"
    )
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_default")
    private boolean isDefault = false;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
