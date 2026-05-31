package pp.paymentservice.kafka.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.commonlib.domain.event.OrderRejectedEvent;
import pp.paymentservice.service.PaymentService;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRejectedEventConsumer {

    private final KafkaReceiver<Long, OrderRejectedEvent> kafkaReceiver;
    private final PaymentService paymentService;

    @PostConstruct
    public void consume() {
        kafkaReceiver.receive()
                .flatMap(record ->
                        processOrderRejectedEvent(record.value())
                                .doOnSuccess(v -> record.receiverOffset().acknowledge())
                                .onErrorResume(e -> {
                                    log.error("Error processing OrderPaidEvent", e);
                                    return Mono.empty();
                                })
                )
                .subscribe();
    }

    public Mono<Void> processOrderRejectedEvent(OrderRejectedEvent event) {
        return paymentService.findByOrderId(event.orderId())
                .flatMap(payment -> {
                    // refund logic (oversimplified)
                    payment.setStatus(PaymentStatus.PAYMENT_REFUNDED);

                    return paymentService.save(payment);
                })
                .then();
    }
}
