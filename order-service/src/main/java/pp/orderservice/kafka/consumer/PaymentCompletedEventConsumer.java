package pp.orderservice.kafka.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pp.commonlib.domain.enums.OrderStatus;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.commonlib.domain.event.PaymentCompletedEvent;
import pp.commonlib.domain.outbox.EventType;
import pp.orderservice.repository.OutboxEventRepository;
import pp.orderservice.service.OrderItemService;
import pp.orderservice.service.OrderService;
import pp.orderservice.utils.OrderUtils;
import pp.orderservice.utils.OutboxEventUtils;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCompletedEventConsumer {

    private final OrderService orderService;
    private final KafkaReceiver<Long, PaymentCompletedEvent> receiver;
    private final OrderItemService orderItemService;
    private final OutboxEventUtils outboxEventUtils;
    private final OutboxEventRepository outboxEventRepository;
    private final TransactionalOperator transactionalOperator;

    @PostConstruct
    public void consume() {
        receiver.receive()
                .flatMap(record ->
                        processPaymentCompletedEvent(record.value())
                                .doOnSuccess(v -> record.receiverOffset().acknowledge())
                                .onErrorResume(e -> {
                                    log.error("Error processing event", e);
                                    return Mono.empty();
                                })
                )
                .subscribe();
    }

    private Mono<Void> processPaymentCompletedEvent(PaymentCompletedEvent event) {
        return Mono.defer(() -> {
                    if (event.status() == PaymentStatus.PAYMENT_FAILED) {
                        log.error("Failed to process payment, payment status: {}",
                                event.status().name());
                        return Mono.error(new RuntimeException(
                                "Payment for order " + event.orderId() + " failed"));
                    }

                    return orderService.updateOrderStatus(
                            event.orderId(),
                            OrderStatus.AWAITING_RESTAURANT_CONFIRMATION
                    );
                })
                .flatMap(order ->
                        orderItemService.getItemsByOrderId(order.getId())
                                .collectList()
                                .map(items -> OrderUtils.buildOrderPaidEvent(order, items))
                                .flatMap(orderPaidEvent -> {
                                    var outboxEvent = outboxEventUtils.create(
                                            event.orderId(),
                                            EventType.PAYMENT_COMPLETED_EVENT,
                                            orderPaidEvent
                                    );

                                    return outboxEventRepository.save(outboxEvent);
                                })
                )
                .as(transactionalOperator::transactional)
                .then();
    }
}
