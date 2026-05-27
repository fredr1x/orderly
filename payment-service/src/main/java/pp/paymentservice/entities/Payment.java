package pp.paymentservice.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.commonlib.domain.enums.PaymentStatus;
import pp.paymentservice.entities.enums.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("payments")
public class Payment {

    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("user_uuid")
    private UUID userUuid;

    @Column("status")
    private PaymentStatus status;

    @Column("currency")
    private Currency currency;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("created_at")
    private Instant createdAt;
}
