package pp.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.userservice.dto.RegisterUserRequest;
import pp.userservice.dto.UserDto;
import pp.userservice.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok().body(authService.getUserByKeycloakId(jwt.getSubject()));
    }

    @PostMapping("/register-user")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest requestDto) {
        return ResponseEntity
                .ok()
                .body(authService.registerUser(requestDto));
    }
}
