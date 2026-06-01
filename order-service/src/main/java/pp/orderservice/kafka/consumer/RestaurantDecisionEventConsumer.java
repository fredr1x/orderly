package pp.orderservice.kafka.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pp.commonlib.domain.enums.OrderStatus;
import pp.commonlib.domain.enums.RestaurantDecision;
import pp.commonlib.domain.event.OrderRejectedEvent;
import pp.commonlib.domain.event.RestaurantDecisionEvent;
import pp.commonlib.domain.outbox.EventType;
import pp.orderservice.repository.OutboxEventRepository;
import pp.orderservice.service.OrderService;
import pp.orderservice.utils.OutboxEventUtils;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantDecisionEventConsumer {

    private final KafkaReceiver<Long, RestaurantDecisionEvent> receiver;

    private final OrderService orderService;
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventUtils outboxEventUtils;
    private final TransactionalOperator transactionalOperator;

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
                        var orderRejectedEvent = OrderRejectedEvent
                                .builder()
                                .orderId(order.getId())
                                .build();

                        var outboxEvent = outboxEventUtils.create(
                                event.orderId(),
                                EventType.ORDER_REJECTED_EVENT,
                                orderRejectedEvent
                        );

                        return outboxEventRepository.save(outboxEvent)
                                .thenReturn(order);
                    }

                    log.info("Saving order with updated status: {}", order.getStatus());
                    return orderService.save(order);
                })
                .as(transactionalOperator::transactional)
                .then();
    }
}
