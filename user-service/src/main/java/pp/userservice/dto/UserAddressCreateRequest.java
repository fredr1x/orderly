package pp.userservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pp.userservice.entity.enums.AddressType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressCreateRequest {

    @NotNull(message = "User id must be not null")
    private Long userId;

    @NotNull(message = "Country must be not null")
    private String country;

    @NotNull(message = "City must be not null")
    private String city;

    @NotNull(message = "Street must be not null")
    private String street;

    @NotNull(message = "House must be not null")
    private String house;

    @NotNull(message = "Apartment must be not null")
    private String apartment;

    @NotNull(message = "Floor must be not null")
    private String floor;

    @Min(value = -180)
    @Max(value = 180)
    @NotNull(message = "Longitude must be not null")
    private double longitude;

    @Min(value = -90)
    @Max(value = 90)
    @NotNull(message = "Latitude must be not null")
    private double latitude;

    private String comment;

    @NotNull(message = "Address type must be not null")
    private AddressType addressType;
}
