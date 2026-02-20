package pp.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pp.userservice.dto.UserAddressCreateRequest;
import pp.userservice.dto.UserAddressDto;
import pp.userservice.service.UserAddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-addresses")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @PostMapping
    public ResponseEntity<UserAddressDto> addUserAddress(@AuthenticationPrincipal Jwt jwt,
                                                         @RequestBody @Validated UserAddressCreateRequest request) {

        return ResponseEntity.ok().body(userAddressService.addUserAddress(jwt.getSubject(), request));
    }

    @DeleteMapping("/{userAddressId}")
    public ResponseEntity<Void> deleteUserAddress(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable("userAddressId") Long userAddressId) {
        userAddressService.deleteUserAddress(jwt.getSubject(), userAddressId);
        return ResponseEntity.noContent().build();
    }
}
