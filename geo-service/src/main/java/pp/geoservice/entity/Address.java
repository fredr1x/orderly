package pp.geoservice.entity;

import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    private Long id;

    @Column("formatted")
    private String formatted;

    @Column("location")
    private Point location;

    @Column("country")
    private String country;

    @Column("province")
    private String province;

    @Column("locality")
    private String locality;

    @Column("street")
    private String street;

    @Column("house")
    private String house;

    @Column("created_at")
    private Instant createdAt;
}
