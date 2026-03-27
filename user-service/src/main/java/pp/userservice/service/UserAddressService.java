package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.userservice.dto.UserAddressCreateRequest;
import pp.userservice.dto.UserAddressDto;
import pp.userservice.mapper.UserAddressMapper;
import pp.userservice.repository.UserAddressRepository;
import pp.userservice.utils.UserAddressUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAddressService {

    private final UserService userService;
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper userAddressMapper;

    @Transactional
    public UserAddressDto addUserAddress(String keycloakId, UserAddressCreateRequest request) {
        var user = userService.getUserByKeycloakId(keycloakId);

        var existingUserAddress = userAddressRepository.findExistingUserAddress(
                                    user.getId(),
                                    request.getAddressType().name(),
                                    request.getLongitude(),
                                    request.getLatitude(),
                                    UserAddressUtils.DEFAULT_SEARCH_RADIUS
        );

        if (existingUserAddress.isPresent()) {
            return userAddressMapper.toUserAddressDto(existingUserAddress.get());
        }

        var userAddress = UserAddressUtils.buildUserAddress(user, request);
        var saved = userAddressRepository.save(userAddress);
        return userAddressMapper.toUserAddressDto(saved);
    }

    @Transactional
    public void deleteUserAddress(String keycloakId, Long userAddressId) {
        var user = userService.getUserByKeycloakId(keycloakId);

        var userAddress = userAddressRepository.findByIdAndUser_Id(userAddressId, user.getId())
                .orElseThrow(() -> new RuntimeException("User address not found"));

        if (!userAddress.getUser().getKeycloakId().toString().equals(keycloakId)) {
            throw new RuntimeException("You can not delete another user's address");
        }

        userAddressRepository.delete(userAddress);
    }
}
