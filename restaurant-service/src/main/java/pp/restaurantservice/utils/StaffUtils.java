package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.commonlib.domain.RegisterUserRequest;
import pp.restaurantservice.dto.BecomePartnerRequest;
import pp.restaurantservice.dto.RestaurantStaffCreateRequest;
import pp.restaurantservice.entity.RestaurantStaff;
import pp.restaurantservice.entity.enums.StaffStatus;

import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class StaffUtils {

    public RestaurantStaff buildRestaurantStaff(UUID userId, RestaurantStaffCreateRequest request) {
        return RestaurantStaff.builder()
                .restaurantId(request.restaurantId())
                .userId(userId)
                .phoneNumber(request.phoneNumber())
                .role(request.role())
                .status(StaffStatus.PENDING_APPROVAL)
                .hiredAt(Instant.now())
                .build();
    }

    public RegisterUserRequest buildRegisterUserRequest(RestaurantStaffCreateRequest request) {
        return RegisterUserRequest.builder()
                .email(request.email())
                .password(request.password())
                .phoneNumber(request.phoneNumber())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public RegisterUserRequest buildRegisterUserRequest(BecomePartnerRequest request) {
        return RegisterUserRequest.builder()
                .email(request.email())
                .password(request.password())
                .phoneNumber(request.phoneNumber())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }
}
