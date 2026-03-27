package pp.restaurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.RestaurantRatingCreateRequest;
import pp.restaurantservice.dto.RestaurantRatingDto;
import pp.restaurantservice.dto.RestaurantRatingStatusUpdateRequest;
import pp.restaurantservice.dto.RestaurantRatingUpdateRequest;
import pp.restaurantservice.service.RestaurantRatingService;
import reactor.core.publisher.Mono;

import static pp.restaurantservice.utils.JwtUtils.extractSubject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RestaurantRatingController {

    private final RestaurantRatingService restaurantRatingService;

    @GetMapping("/{id}")
    public Mono<RestaurantRatingDto> findById(@PathVariable Long id) {
        return restaurantRatingService.findDtoById(id);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public Mono<Page<RestaurantRatingDto>> getRatings(@PathVariable Long restaurantId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return restaurantRatingService.findAllByRestaurant(restaurantId, page, size);
    }

    @GetMapping("/restaurant/{restaurantId}/review")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<Page<RestaurantRatingDto>> getRatingsForReview(@AuthenticationPrincipal Jwt jwt,
                                                               @PathVariable Long restaurantId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size) {
        var currentUserId = extractSubject(jwt);
        return restaurantRatingService.findAllForReview(currentUserId, restaurantId, page, size);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public Mono<RestaurantRatingDto> createRating(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable Long restaurantId,
                                                  @RequestBody @Valid RestaurantRatingCreateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantRatingService.createRating(currentUserId, restaurantId, request);
    }

    @PatchMapping("/{id}")
    public Mono<RestaurantRatingDto> updateRating(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable Long id,
                                                  @RequestBody @Valid RestaurantRatingUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantRatingService.updateRating(currentUserId, id, request);
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<RestaurantRatingDto> updateStatus(@AuthenticationPrincipal Jwt jwt,
                                                  @PathVariable Long id,
                                                  @RequestBody @Valid RestaurantRatingStatusUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return restaurantRatingService.updateStatus(currentUserId, id, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRating(@AuthenticationPrincipal Jwt jwt,
                                   @PathVariable Long id) {
        var currentUserId = extractSubject(jwt);
        return restaurantRatingService.deleteRating(currentUserId, id);
    }
}
