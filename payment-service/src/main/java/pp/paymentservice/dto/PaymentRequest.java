package pp.paymentservice.dto;

import pp.paymentservice.entities.enums.Currency;

import java.math.BigDecimal;

public record PaymentRequest(
   Long orderId,
   BigDecimal totalAmount,
   Currency currency
) {}
