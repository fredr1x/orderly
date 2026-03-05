package pp.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.commonlib.domain.UserDto;
import pp.userservice.dto.ChangePasswordRequest;
import pp.userservice.dto.ChangeUserNameRequest;
import pp.userservice.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal Jwt jwt,
                                               @RequestBody ChangePasswordRequest request) {

        userService.changePassword(jwt.getSubject(), request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/change-name")
    public ResponseEntity<UserDto> changeName(@AuthenticationPrincipal Jwt jwt,
                                              @RequestBody ChangeUserNameRequest request) {

        return ResponseEntity
                .ok()
                .body(userService.changeUserName(jwt.getSubject(), request));
    }
}
