package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.commonlib.domain.dto.OrderItemDto;
import pp.restaurantservice.dto.RestaurantOrderItemDto;
import pp.restaurantservice.entity.RestaurantOrderItem;
import pp.restaurantservice.repository.RestaurantOrderItemRepository;
import pp.restaurantservice.utils.RestaurantOrderItemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantOrderItemService {

    private final RestaurantOrderItemRepository restaurantOrderItemRepository;

    public Flux<RestaurantOrderItemDto> getRestaurantOrderItemsByRestaurantOrderId(Long restaurantOrderId) {
        return restaurantOrderItemRepository.findAllByRestaurantOrderId(restaurantOrderId)
                .map(RestaurantOrderItemUtils::buildRestaurantOrderItemDto);
    }
}
