package pp.orderservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pp.commonlib.domain.outbox.OutboxStatus;
import pp.orderservice.outbox.OutboxEvent;
import reactor.core.publisher.Flux;

@Repository
public interface OutboxEventRepository extends R2dbcRepository<OutboxEvent, Long> {
    Flux<OutboxEvent> findAllByStatus(OutboxStatus status);
}
