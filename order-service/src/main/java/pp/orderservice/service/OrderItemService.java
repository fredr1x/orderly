package pp.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.orderservice.dto.OrderItemCreateRequest;
import pp.orderservice.dto.OrderItemCreateResponse;
import pp.orderservice.repository.OrderItemRepository;
import pp.orderservice.utils.OrderItemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public Flux<OrderItemDto> getItemsByOrderId(Long orderId) {
        return orderItemRepository
                .findAllByOrderId(orderId)
                .map(OrderItemUtils::buildOrderItemDto);
    }
}
