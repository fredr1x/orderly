package pp.commonlib.domain.event;

import lombok.Builder;

@Builder
public record OrderRejectedEvent(
        Long orderId
) {}
