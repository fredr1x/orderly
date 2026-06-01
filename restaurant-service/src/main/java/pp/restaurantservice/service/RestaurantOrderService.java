package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pp.commonlib.domain.enums.RestaurantDecision;
import pp.commonlib.domain.event.OrderPaidEvent;
import pp.commonlib.domain.outbox.EventType;
import pp.restaurantservice.dto.RestaurantOrderDto;
import pp.restaurantservice.entity.enums.RestaurantOrderStatus;
import pp.restaurantservice.repository.OutboxEventRepository;
import pp.restaurantservice.repository.RestaurantOrderItemRepository;
import pp.restaurantservice.repository.RestaurantOrderRepository;
import pp.restaurantservice.utils.OutboxEventUtils;
import pp.restaurantservice.utils.RestaurantOrderItemUtils;
import pp.restaurantservice.utils.RestaurantOrderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantOrderService {

    private final RestaurantOrderRepository restaurantOrderRepository;
    private final RestaurantOrderItemRepository restaurantOrderItemRepository;

    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantOrderItemService restaurantOrderItemService;
    private final OutboxEventUtils outboxEventUtils;
    private final OutboxEventRepository outboxEventRepository;
    private final TransactionalOperator transactionalOperator;

    public Flux<RestaurantOrderDto> getAllRestaurantOrdersByStatus(
            String currentUserId,
            Long restaurantId,
            RestaurantOrderStatus status
    ) {
        var currentUserUuid = UUID.fromString(currentUserId);

        return restaurantStaffService
                .validateStaffRoleAndSameRestaurant(currentUserUuid, restaurantId)
                .thenMany(
                        restaurantOrderRepository
                                .findAllByRestaurantIdAndStatus(restaurantId, status)
                                .flatMap(order ->
                                        restaurantOrderItemService
                                                .getRestaurantOrderItemsByRestaurantOrderId(order.getId())
                                                .collectList()
                                                .map(items ->
                                                        RestaurantOrderUtils.buildRestaurantOrderDto(order, items)
                                                )
                                )
                );
    }

    public Mono<Void> createOrderFromEvent(OrderPaidEvent event) {
        return Mono.defer(() -> {
            var restaurantOrder = RestaurantOrderUtils.buildRestaurantOrder(event);
            return restaurantOrderRepository.save(restaurantOrder)
                    .flatMap(order -> {
                        var restaurantOrderItems = event.items()
                                .stream()
                                .map(
                                        item -> RestaurantOrderItemUtils.buildRestaurantOrderItem(order.getId(), item)
                                )
                                .toList();

                        return restaurantOrderItemRepository.saveAll(restaurantOrderItems)
                                .then();
                    });
        });
    }

    public Mono<RestaurantOrderDto> updateRestaurantOrderStatus(
            String currentUserId,
            Long restaurantOrderId,
            RestaurantOrderStatus status
    ) {
        return restaurantOrderRepository.findById(restaurantOrderId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Restaurant order with id: " + restaurantOrderId + " not found")
                ))
                .flatMap(order -> {
                    var currentUserUuid = UUID.fromString(currentUserId);

                    return restaurantStaffService
                            .validateStaffRoleAndSameRestaurant(currentUserUuid, order.getRestaurantId())
                            .then(Mono.defer(() -> {
                                order.setStatus(status);
                                order.setModified_by(currentUserUuid);
                                order.setModified_at(Instant.now());

                                return restaurantOrderRepository.save(order);
                            }))
                            .flatMap(savedOrder ->
                                    restaurantOrderItemService
                                            .getRestaurantOrderItemsByRestaurantOrderId(savedOrder.getId())
                                            .collectList()
                                            .map(items ->
                                                    RestaurantOrderUtils.buildRestaurantOrderDto(savedOrder, items)
                                            )
                            );
                })
                .flatMap(dto ->
                        Mono.defer(() -> {
                            var restaurantDecisionEvent = RestaurantOrderUtils.buildRestaurantDecisionEvent(
                                    dto.orderId(),
                                    dto.status() == RestaurantOrderStatus.APPROVED
                                            ? RestaurantDecision.APPROVED
                                            : RestaurantDecision.REJECTED
                            );

                            var outboxEvent = outboxEventUtils.create(
                                    dto.orderId(),
                                    EventType.RESTAURANT_DECISION_EVENT,
                                    restaurantDecisionEvent
                            );

                            return outboxEventRepository.save(outboxEvent)
                                    .thenReturn(dto);
                        })
                )
                .as(transactionalOperator::transactional);
    }
}
