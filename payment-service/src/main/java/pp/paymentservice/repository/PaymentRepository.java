package pp.paymentservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.paymentservice.entities.Payment;

@Repository
public interface PaymentRepository extends R2dbcRepository<Payment, Long> {
}
