package pp.restaurantservice.kafka.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.event.OrderPaidEvent;
import pp.restaurantservice.service.RestaurantOrderService;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPaidEventConsumer {

    private final KafkaReceiver<Long, OrderPaidEvent> kafkaReceiver;
    private final RestaurantOrderService restaurantOrderService;

    @PostConstruct
    public void consume() {
        kafkaReceiver.receive()
                .flatMap(record ->
                    processOrderPaidEvent(record.value())
                            .doOnSuccess(v -> record.receiverOffset().acknowledge())
                            .onErrorResume(e -> {
                                log.error("Error processing OrderPaidEvent", e);
                                return Mono.empty();
                            })
                )
                .subscribe();
    }

    public Mono<Void> processOrderPaidEvent(OrderPaidEvent event) {
        return restaurantOrderService.createOrderFromEvent(event);
    }
}
