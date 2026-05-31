package pp.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.enums.OrderStatus;
import pp.orderservice.dto.OrderCreateRequest;
import pp.orderservice.dto.OrderCreateResponse;
import pp.orderservice.dto.OrderDto;
import pp.orderservice.entity.Order;
import pp.orderservice.entity.OrderItem;
import pp.orderservice.repository.OrderItemRepository;
import pp.orderservice.repository.OrderRepository;
import pp.orderservice.utils.OrderItemUtils;
import pp.orderservice.utils.OrderUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order by id " + id + " not found")));
    }

    public Mono<OrderDto> getOrderById(String currentUserId, Long orderId) {
        return findById(orderId)
                .flatMap(order -> {
                    var currentUserUuid = UUID.fromString(currentUserId);
                    if (order.getUserUuid() != currentUserUuid) return Mono.error(new RuntimeException("Not enough permissions"));

                    return orderItemRepository.findAllByOrderId(order.getId())
                            .map(OrderItemUtils::buildOrderItemDto)
                            .collectList()
                            .map(items -> OrderUtils.buildOrderDto(order, items));
                });
    }

    public Mono<OrderCreateResponse> createOrder(String currentUserId, OrderCreateRequest request) {
        return Mono.defer(() -> {
            var order = OrderUtils.buildOrder(UUID.fromString(currentUserId), request);

            return orderRepository.save(order);
        })
        .flatMap(order -> {
            List<OrderItem> orderItems = request.items().stream()
                    .map(itemRequest -> OrderItemUtils.buildOrderItem(order.getId(), itemRequest))
                    .toList();

            return orderItemRepository.saveAll(orderItems)
                    .map(OrderItemUtils::buildOrderItemCreateResponse)
                    .collectList()
                    .map(items -> OrderUtils.buildOrderCreateResponse(order, items));
        });
    }

    public Mono<Order> updateOrderStatus(Long id, OrderStatus status) {
        return findById(id)
                .flatMap(order -> updateOrderStatus(order, status));
    }

    public Mono<Order> updateOrderStatus(Order order, OrderStatus orderStatus) {
        return Mono.defer(() -> {
            order.setStatus(orderStatus);
            return orderRepository.save(order);
        });
    }

    public Mono<Void> save(Order order) {
        return orderRepository.save(order)
                .then();
    }
}
