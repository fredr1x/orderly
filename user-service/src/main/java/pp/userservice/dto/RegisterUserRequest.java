package pp.userservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
