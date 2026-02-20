package pp.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import pp.userservice.entity.enums.AddressType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDto {
    private Long userId;
    private String country;
    private String city;
    private String street;
    private String house;
    private String apartment;
    private String floor;
    private String comment;
    private AddressType addressType;
    private boolean isDefault;
    private boolean isActive;
}
