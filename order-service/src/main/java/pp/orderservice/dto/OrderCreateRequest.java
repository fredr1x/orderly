package pp.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
        Long restaurantId,
        String address,
        BigDecimal totalAmount,
        List<OrderItemCreateRequest> items
)
{}
