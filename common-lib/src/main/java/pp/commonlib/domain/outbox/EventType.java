package pp.commonlib.domain.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    PAYMENT_COMPLETED_EVENT("payment-events"),
    ORDER_PAID_EVENT("order-events"),
    RESTAURANT_DECISION_EVENT("restaurant-events"),
    ORDER_REJECTED_EVENT("order-events");

    private final String topicName;
}
