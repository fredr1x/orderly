package pp.userservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String avatarPhotoUrl;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}
