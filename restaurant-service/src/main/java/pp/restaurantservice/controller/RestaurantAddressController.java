package pp.restaurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.RestaurantAddressCreateRequest;
import pp.restaurantservice.dto.RestaurantAddressDto;
import pp.restaurantservice.dto.RestaurantAddressUpdateRequest;
import pp.restaurantservice.service.RestaurantAddressService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pp.restaurantservice.utils.JwtUtils.extractSubject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class RestaurantAddressController {

    private final RestaurantAddressService restaurantAddressService;

    @GetMapping("/{id}")
    public Mono<RestaurantAddressDto> getById(@PathVariable Long id) {
        return restaurantAddressService.findDtoById(id);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public Mono<RestaurantAddressDto> getByRestaurant(@PathVariable Long restaurantId) {
        return restaurantAddressService.findByRestaurantId(restaurantId);
    }

    @GetMapping("/brand/{brandId}")
    public Flux<RestaurantAddressDto> getAllByBrand(@PathVariable Long brandId) {
        return restaurantAddressService.findAllByBrandId(brandId);
    }


    @PostMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'RESTAURANT_MANAGER')")
    public Mono<RestaurantAddressDto> createAddress(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable Long restaurantId,
                                                    @RequestBody @Valid RestaurantAddressCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantAddressService.createAddress(currentUserId, restaurantId, request);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'RESTAURANT_MANAGER')")
    public Mono<RestaurantAddressDto> updateAddress(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable Long id,
                                                    @RequestBody @Valid RestaurantAddressUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantAddressService.updateAddress(currentUserId, id, request);
    }
}
