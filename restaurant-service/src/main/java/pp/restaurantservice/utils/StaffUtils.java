package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.RegisterUserRequest;
import pp.restaurantservice.dto.BecomePartnerRequest;
import pp.restaurantservice.dto.RestaurantStaffCreateRequest;
import pp.restaurantservice.entity.RestaurantStaff;
import pp.restaurantservice.entity.enums.StaffRole;
import pp.restaurantservice.entity.enums.StaffStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class StaffUtils {

    public RestaurantStaff buildRestaurantOwner(UUID userId, String phoneNumber, Long restaurantId) {
        return RestaurantStaff.builder()
                .userId(userId)
                .phoneNumber(phoneNumber)
                .status(StaffStatus.ACTIVE)
                .role(StaffRole.ROLE_RESTAURANT_OWNER)
                .restaurantId(restaurantId)
                .hiredAt(Instant.now())
                .build();
    }

    public RestaurantStaff buildRestaurantStaff(UUID userId, RestaurantStaffCreateRequest request) {
        return RestaurantStaff.builder()
                .restaurantId(request.getRestaurantId())
                .userId(userId)
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(StaffStatus.PENDING_APPROVAL)
                .hiredAt(Instant.now())
                .build();
    }

    public RegisterUserRequest buildRegisterUserRequest(RestaurantStaffCreateRequest request) {
        return RegisterUserRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

    public RegisterUserRequest buildRegisterUserRequest(BecomePartnerRequest request) {
        return RegisterUserRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }
}
