package pp.commonlib.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
}
