package pp.restaurantservice.repository.projection;

import java.math.BigDecimal;

public interface RestaurantRatingAvg {
    Long getRestaurantId();
    BigDecimal getAvgRating();
}
