package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.userservice.dto.RegisterUserRequest;
import pp.userservice.dto.UserDto;
import pp.userservice.exception.EmailInUseException;
import pp.userservice.mapper.UserMapper;
import pp.userservice.repository.UserRepository;
import pp.userservice.utils.RoleUtils;
import pp.userservice.utils.UserUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final RoleService roleService;

    public UserDto getUserByKeycloakId(String keycloakId) {
        return userMapper.toUserDto(
                userRepository.findByKeycloakId(UUID.fromString(keycloakId))
                .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    @Transactional
    public UserDto registerUser(RegisterUserRequest requestDto) {
        validateEmail(requestDto.getEmail());

        String keycloakId = null;
        try {
            keycloakId = keycloakAdminService.createUser(requestDto);

            var user = UserUtils.buildUser(requestDto, keycloakId);
            var userRole = roleService.findRoleByValue(RoleUtils.userRoleValue);
            user.addRole(userRole);

            var saved = userRepository.save(user);
            return userMapper.toUserDto(saved);
        } catch (Exception e) {
            keycloakAdminService.deleteUserOnFail(keycloakId);
            throw new RuntimeException(e);
        }
    }

    private void validateEmail(String email) {
        var optionalUserByEmail = userRepository.findByEmail(email);

        if (optionalUserByEmail.isPresent()) {
            throw new EmailInUseException("Email already in use");
        }
    }
}
