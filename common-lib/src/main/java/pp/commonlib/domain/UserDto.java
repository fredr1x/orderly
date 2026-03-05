package pp.commonlib.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private UUID keycloakId;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String avatarPhotoUrl;
}
