package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.RestaurantRatingCreateRequest;
import pp.restaurantservice.dto.RestaurantRatingDto;
import pp.restaurantservice.dto.RestaurantRatingStatusUpdateRequest;
import pp.restaurantservice.dto.RestaurantRatingUpdateRequest;
import pp.restaurantservice.entity.RestaurantRating;
import pp.restaurantservice.entity.enums.RatingStatus;
import pp.restaurantservice.mapper.RestaurantRatingMapper;
import pp.restaurantservice.repository.RestaurantRatingRepository;
import pp.restaurantservice.utils.RestaurantRatingUtils;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.fromString;

@Service
@RequiredArgsConstructor
public class RestaurantRatingService {

    private final RestaurantRatingMapper restaurantRatingMapper;
    private final RestaurantRatingRepository restaurantRatingRepository;

    private final RestaurantService restaurantService;
    private final RestaurantStaffService restaurantStaffService;

    public Mono<RestaurantRating> findById(Long id) {
        return restaurantRatingRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Restaurant rating not found")));
    }

    public Mono<RestaurantRatingDto> findDtoById(Long id) {
        return findById(id)
                .map(restaurantRatingMapper::toRestaurantRatingDto);
    }

    public Mono<Page<RestaurantRatingDto>> findAllByRestaurant(Long restaurantId, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return restaurantService.findById(restaurantId)
                .then(restaurantRatingRepository.findAllByRestaurantIdAndStatus(restaurantId, RatingStatus.APPROVED, pageable)
                        .map(restaurantRatingMapper::toRestaurantRatingDto)
                        .collectList()
                        .zipWith(restaurantRatingRepository.countByRestaurantId(restaurantId))
                        .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()))
                );
    }

    public Mono<Page<RestaurantRatingDto>> findAllForReview(String currentUserId, Long restaurantId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        return restaurantService.findById(restaurantId)
                .then(restaurantStaffService.validateManager(currentUserId, restaurantId)
                        .then(restaurantRatingRepository.findAllByRestaurantIdAndStatus(restaurantId, RatingStatus.IN_REVIEW, pageable)
                                .map(restaurantRatingMapper::toRestaurantRatingDto)
                                .collectList()
                                .zipWith(restaurantRatingRepository.countByRestaurantIdAndStatus(restaurantId, RatingStatus.IN_REVIEW))
                                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()))
                        )
                );
    }

    // todo validate if user has orders from restaurant (order-service client)
    public Mono<RestaurantRatingDto> createRating(String currentUserId, Long restaurantId, RestaurantRatingCreateRequest request) {
        var currentUserUuid = fromString(currentUserId);
        return restaurantService.findById(restaurantId)
                .then(restaurantRatingRepository.existsByClientUserIdAndRestaurantIdAndStatusNot(currentUserUuid, restaurantId, RatingStatus.DELETED))
                .flatMap(exists -> {
                    if (exists) return Mono.error(() -> new RuntimeException("You have already rated this restaurant"));

                    return restaurantRatingRepository.save(RestaurantRatingUtils.buildRestaurantRating(currentUserUuid, restaurantId, request))
                            .map(restaurantRatingMapper::toRestaurantRatingDto);
                });
    }

    public Mono<RestaurantRatingDto> updateRating(String currentUserId, Long id, RestaurantRatingUpdateRequest request) {
        return findById(id)
                .flatMap(restaurantRating ->
                    validateRatingOwner(fromString(currentUserId), restaurantRating)
                            .then(Mono.defer(() -> {

                                if (request.comment() != null &&
                                    !restaurantRating.getComment().equals(request.comment())) {

                                    restaurantRating.setStatus(RatingStatus.IN_REVIEW);
                                }

                                restaurantRatingMapper.updateRestaurantRatingFromRequest(request, restaurantRating);
                                restaurantRating.setUpdatedAt(Instant.now());

                                return restaurantRatingRepository.save(restaurantRating)
                                        .map(restaurantRatingMapper::toRestaurantRatingDto);
                            }))
                );
    }

    public Mono<RestaurantRatingDto> updateStatus(String currentUserId, Long id, RestaurantRatingStatusUpdateRequest request) {
        return findById(id)
                .flatMap(rating ->
                        restaurantStaffService.validateManager(currentUserId, rating.getRestaurantId())
                                .thenReturn(rating)
                )
                .doOnNext(restaurantRating -> restaurantRating.setStatus(request.status()))
                .flatMap(restaurantRatingRepository::save)
                .map(restaurantRatingMapper::toRestaurantRatingDto);
    }

    public Mono<Void> deleteRating(String currentUserId, Long id) {
        return findById(id)
                .flatMap(restaurantRating ->
                    validateRatingOwner(fromString(currentUserId), restaurantRating)
                            .then(Mono.defer(() -> {
                                restaurantRating.setStatus(RatingStatus.DELETED);
                                return restaurantRatingRepository.save(restaurantRating).then();
                            }))
                );
    }

    private Mono<Void> validateRatingOwner(UUID currentUserId, RestaurantRating restaurantRating) {
        if (!restaurantRating.getClientUserId().equals(currentUserId)) {
            return Mono.error(() -> new RuntimeException("You can not change another user's rating"));
        }

        return Mono.empty();
    }
}
