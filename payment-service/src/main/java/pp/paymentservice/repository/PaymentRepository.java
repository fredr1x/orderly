package pp.paymentservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.paymentservice.dto.PaymentDto;
import pp.paymentservice.entities.Payment;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface PaymentRepository extends R2dbcRepository<Payment, Long> {
    Mono<Payment> findByOrderId(Long orderId);
}
