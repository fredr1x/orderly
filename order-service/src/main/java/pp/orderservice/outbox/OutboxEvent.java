package pp.orderservice.outbox;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pp.commonlib.domain.outbox.EventType;
import pp.commonlib.domain.outbox.OutboxStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("outbox_events")
public class OutboxEvent {

    @Id
    private Long id;

    @Column("event_id")
    private Long eventId;

    @Column("event_type")
    private EventType eventType;

    @Column("payload")
    private String payload;

    @Column("status")
    private OutboxStatus status;

    @Column("created_at")
    private Instant createdAt;

    @Column("sent_at")
    private Instant sentAt;
}
