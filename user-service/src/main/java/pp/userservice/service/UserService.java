package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.userservice.dto.ChangePasswordRequest;
import pp.userservice.dto.ChangeUserNameRequest;
import pp.userservice.dto.UserDto;
import pp.userservice.entity.User;
import pp.userservice.mapper.UserMapper;
import pp.userservice.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserMapper userMapper;

    //jwt.subject() = keycloakId
    @Transactional
    public void changePassword(String keycloakId, ChangePasswordRequest request) {
        var user = getUserByKeycloakId(keycloakId);

        var username = user.getEmail();

        try {
            keycloakAdminService.authenticate(username, request.getOldPassword());
            keycloakAdminService.changePassword(keycloakId, request.getNewPassword());

            user.setLastModifiedAt(Instant.now());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to change password", e);
        }
    }

    @Transactional
    public UserDto changeUserName(String keycloakId, ChangeUserNameRequest request) {
        var userToUpdate = getUserByKeycloakId(keycloakId);

        userToUpdate.setFirstName(request.getFirstName());
        userToUpdate.setLastName(request.getLastName());
        userToUpdate.setLastModifiedAt(Instant.now());

        var updatedUser = userRepository.save(userToUpdate);
        return userMapper.toUserDto(updatedUser);
    }

    public User getUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(UUID.fromString(keycloakId))
                .orElseThrow(() -> new RuntimeException("User not found with keycloakId: " + keycloakId));
    }
}
