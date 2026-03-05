package pp.restaurantservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BecomePartnerRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    private String brandName;
    private String brandDescription;
    private String imageProfileLink;
}
