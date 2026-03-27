package pp.commonlib.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String formatted;
    private String country;
    private String province;
    private String locality;
    private String street;
    private String house;
    private double longitude;
    private double latitude;
}
