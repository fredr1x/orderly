package pp.orderservice.kafka.publisher;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.event.OrderRejectedEvent;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRejectedEventPublisher {

    private final KafkaSender<Long, OrderRejectedEvent> kafkaSender;

    @Value("${kafka.order-topic:order-events}")
    private String topic;

    public Mono<Void> publishOrderRejectedEvent(OrderRejectedEvent event) {
        log.info("Publishing OrderRejectedEvent: {}", event);

        ProducerRecord<Long, OrderRejectedEvent> record = new ProducerRecord<>(
                topic,
                event.orderId(),
                event
        );

        return kafkaSender
                .send(Mono.just(SenderRecord.create(record, event.orderId())))
                .doOnNext(result -> {
                    if (result.exception() != null) {
                        log.error("Failed to send OrderRejectedEvent for order: {}", event.orderId(), result.exception());
                    } else {
                        log.info("OrderRejectedEvent send successfully for order: {}", event.orderId());
                    }
                })
                .then();
    }

    @PreDestroy
    public void close() {
        kafkaSender.close();
    }
}
