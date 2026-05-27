package pp.orderservice.kafka.publisher;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.event.OrderPaidEvent;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPaidEventPublisher {

    private final KafkaSender<Long, OrderPaidEvent> kafkaSender;

    @Value("${kafka.order-topic:order-events}")
    private String topic;

    public Mono<Void> publishOrderPaidEvent(OrderPaidEvent event) {
        log.info("Publishing order event with status: {}", event.status());

        ProducerRecord<Long, OrderPaidEvent> record = new ProducerRecord<>(
                topic,
                event.orderId(),
                event
        );

        return kafkaSender
                .send(Mono.just(SenderRecord.create(record, event.orderId())))
                .doOnNext(result -> {
                    if (result.exception() != null) {
                        log.error("Failed to send order paid event for order with id: {}, exception:", event.orderId(), result.exception());
                    } else {
                        log.info("OrderPaidEvent published successfully for order with id: {}", event.orderId());
                    }
                })
                .then();
    }

    @PreDestroy
    public void close() {
        kafkaSender.close();
    }
}
