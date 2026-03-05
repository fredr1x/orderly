package pp.userservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.RegisterUserRequest;
import pp.userservice.entity.User;

import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class UserUtils {

    public static User buildUser(RegisterUserRequest requestDto, String keycloakId) {
        return User.builder()
                .keycloakId(UUID.fromString(keycloakId))
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .isActive(true)
                .createdAt(Instant.now())
                .build();
    }
}
