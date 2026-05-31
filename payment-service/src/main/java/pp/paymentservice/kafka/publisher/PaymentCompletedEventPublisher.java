package pp.paymentservice.kafka.publisher;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.event.PaymentCompletedEvent;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCompletedEventPublisher {

    private final KafkaSender<Long, PaymentCompletedEvent> kafkaSender;

    @Value("${kafka.payment-topic:payment-events}")
    private String topic;

    public Mono<Void> publishPaymentEvent(PaymentCompletedEvent event) {
        log.info("Publishing payment event with status: {}", event.status());

        ProducerRecord<Long, PaymentCompletedEvent> record = new ProducerRecord<>(
                topic,
                event.orderId(),
                event
        );

        return kafkaSender
                .send(Mono.just(SenderRecord.create(record, event.paymentId())))
                .doOnNext(result -> {
                    if (result.exception() != null) {
                        log.error("Failed to send payment event", result.exception());
                    } else {
                        log.info("Payment event published successfully for payment with id: {}", event.paymentId());
                    }
                })
                .then();
    }

    @PreDestroy
    public void close() {
        kafkaSender.close();
    }
}
