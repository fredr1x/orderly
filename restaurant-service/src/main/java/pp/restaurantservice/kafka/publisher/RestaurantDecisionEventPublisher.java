package pp.restaurantservice.kafka.publisher;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.event.RestaurantDecisionEvent;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantDecisionEventPublisher {

    private final KafkaSender<Long, RestaurantDecisionEvent> kafkaSender;

    @Value("${kafka.restaurant-topic}")
    private String topic;

    public Mono<Void> publishRestaurantDecisionEvent(RestaurantDecisionEvent event) {
        log.info("Publishing restaurant decision event with decision: {}", event.decision());

        ProducerRecord<Long, RestaurantDecisionEvent> record = new ProducerRecord<>(
                topic,
                event.orderId(),
                event
        );

        return kafkaSender
                .send(Mono.just(SenderRecord.create(record, event.orderId())))
                .doOnNext(result -> {
                    if (result.exception() != null) {
                        log.error("Failed to send RestaurantDecisionEvent with order id: {}, exception:", event.orderId(), result.exception());
                    } else {
                        log.info("RestaurantDecisionEvent published successfully for order with id: {}", event.orderId());
                    }
                })
                .then();
    }

    @PreDestroy
    public void close() {
        kafkaSender.close();
    }
}
