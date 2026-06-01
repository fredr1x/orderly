package pp.paymentservice.outbox.poller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pp.commonlib.domain.outbox.OutboxStatus;
import pp.paymentservice.outbox.OutboxEvent;
import pp.paymentservice.repository.OutboxEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller implements ApplicationRunner {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaSender<Long, String> outboxKafkaSender;

    @Override
    public void run(ApplicationArguments args) {
        Flux.interval(Duration.ofSeconds(5))
                .onBackpressureDrop()
                .flatMap(tick -> pollAndPublish(), 1)
                .subscribe(
                        null,
                        err -> log.error("Outbox poller fatal error", err)
                );
    }

    private Flux<Void> pollAndPublish() {
        return outboxEventRepository.findAllByStatus(OutboxStatus.PENDING)
                .flatMap(this::publish);
    }

    private Mono<Void> publish(OutboxEvent event) {
        SenderRecord<Long, String, Long> record = SenderRecord.create(
                new ProducerRecord<>(
                        event.getEventType().getTopicName(),
                        event.getEventId(),
                        event.getPayload()
                ),
                event.getId()
        );

        return outboxKafkaSender.send(Mono.just(record))
                .next()
                .flatMap(result -> {
                    if (result.exception() != null) {
                        return markAsFailed(event, result.exception());
                    }
                    return markAsSent(event);
                })
                .onErrorResume(ex -> markAsFailed(event, ex));
    }

    private Mono<Void> markAsSent(OutboxEvent event) {
        event.setStatus(OutboxStatus.SENT);
        event.setSentAt(Instant.now());
        return outboxEventRepository.save(event)
                .doOnSuccess(e -> log.info("Outbox event {} sent successfully", e.getId()))
                .then();
    }

    private Mono<Void> markAsFailed(OutboxEvent event, Throwable ex) {
        log.error("Failed to publish outbox event {}, type={}", event.getId(), event.getEventType(), ex);
        event.setStatus(OutboxStatus.FAILED);
        return outboxEventRepository.save(event).then();
    }
}
