package pp.orderservice.kafka.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.enums.OrderStatus;
import pp.commonlib.domain.enums.RestaurantDecision;
import pp.commonlib.domain.event.OrderRejectedEvent;
import pp.commonlib.domain.event.RestaurantDecisionEvent;
import pp.orderservice.kafka.publisher.OrderRejectedEventPublisher;
import pp.orderservice.service.OrderService;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantDecisionEventConsumer {

    private final KafkaReceiver<Long, RestaurantDecisionEvent> receiver;
    private final OrderRejectedEventPublisher orderRejectedEventPublisher;

    private final OrderService orderService;

    @PostConstruct
    public void consume() {
        receiver.receive()
                .flatMap(record ->
                        processRestaurantDecisionEvent(record.value())
                                .doOnSuccess(v -> record.receiverOffset().acknowledge())
                                .onErrorResume(e -> {
                                    log.error("Error processing event RestaurantDecisionEvent: {}", record.value(), e);
                                    return Mono.empty();
                                })
                )
                .subscribe();
    }

    private Mono<Void> processRestaurantDecisionEvent(RestaurantDecisionEvent event) {
        return orderService.findById(event.orderId())
                .flatMap(order -> {
                    log.info("Processing received RestaurantDecisionEvent with status: {}", event.decision().name());
                    if (event.decision() == RestaurantDecision.APPROVED)
                        order.setStatus(OrderStatus.RESTAURANT_PREPARING);

                    else if (event.decision() == RestaurantDecision.REJECTED) {
                        order.setStatus(OrderStatus.RESTAURANT_REJECTED);
                        return orderRejectedEventPublisher
                                .publishOrderRejectedEvent(
                                        OrderRejectedEvent
                                                .builder()
                                                .orderId(order.getId())
                                                .build()
                                )
                                .thenReturn(order);
                    }

                    log.info("Saving order with updated status: {}", order.getStatus());
                    return orderService.save(order);
                })
                .then();
    }
}
