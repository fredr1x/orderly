package pp.commonlib.domain.event;

import lombok.Builder;
import pp.commonlib.domain.enums.RestaurantDecision;

@Builder
public record RestaurantDecisionEvent(
        Long orderId,
        RestaurantDecision decision,
        String reason
)
{}
