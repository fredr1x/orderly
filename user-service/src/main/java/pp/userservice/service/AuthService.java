package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.commonlib.domain.RegisterUserRequest;
import pp.commonlib.domain.UserDto;
import pp.userservice.entity.enums.Value;
import pp.userservice.exception.EmailInUseException;
import pp.userservice.exception.KeycloakException;
import pp.userservice.mapper.UserMapper;
import pp.userservice.repository.UserRepository;
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
            var userRole = roleService.findRoleByValue(Value.ROLE_USER);
            user.addRole(userRole);

            var saved = userRepository.save(user);
            return userMapper.toUserDto(saved);
        } catch (Exception e) {
            keycloakAdminService.deleteUserOnFail(keycloakId);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public UserDto registerOwner(RegisterUserRequest requestDto) {
        validateEmail(requestDto.getEmail());
        var userDto = registerUser(requestDto);
        var roleOwner = roleService.findRoleByValue(Value.ROLE_RESTAURANT_OWNER);
        roleService.assignRole(userDto.getId(), roleOwner.getId());

        try {
            keycloakAdminService.assignRealmRole(userDto.getKeycloakId().toString(), roleOwner.getValue().name());
            return userDto;
        } catch (Exception e) {
            throw new KeycloakException("Failed to assign roles to owner");
        }
    }

    @Transactional
    public UserDto registerManager(RegisterUserRequest requestDto) {
        validateEmail(requestDto.getEmail());
        var userDto = registerUser(requestDto);
        var roleManager = roleService.findRoleByValue(Value.ROLE_RESTAURANT_MANAGER);
        roleService.assignRole(userDto.getId(), roleManager.getId());

        try {
            keycloakAdminService.assignRealmRole(userDto.getKeycloakId().toString(), roleManager.getValue().name());
            return userDto;
        } catch (Exception e) {
            throw new KeycloakException("Failed to assign roles to manager");
        }
    }

    private void validateEmail(String email) {
        var optionalUserByEmail = userRepository.findByEmail(email);

        if (optionalUserByEmail.isPresent()) {
            throw new EmailInUseException("Email already in use");
        }
    }
}
