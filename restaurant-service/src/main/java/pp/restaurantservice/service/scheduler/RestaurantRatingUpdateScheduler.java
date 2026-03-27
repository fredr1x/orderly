package pp.restaurantservice.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pp.restaurantservice.repository.RestaurantRatingRepository;
import pp.restaurantservice.repository.RestaurantRepository;

import java.time.Instant;


@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantRatingUpdateScheduler {

    private final RestaurantRatingRepository restaurantRatingRepository;
    private final RestaurantRepository restaurantRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateRestaurantRating() {
        restaurantRatingRepository.findAverageRatingsByRestaurant()
                .flatMap(avg -> restaurantRepository.findById(avg.getRestaurantId())
                        .flatMap(restaurant -> {
                            restaurant.setAverageRating(avg.getAvgRating());
                            restaurant.setUpdatedAt(Instant.now());
                            return restaurantRepository.save(restaurant);
                        })
                )
                .doOnComplete(() -> log.info("Rating updated successfully"))
                .doOnError(e -> log.error("Error updating ratings", e))
                .subscribe();
    }
}
