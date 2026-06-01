package pp.orderservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pp.commonlib.domain.outbox.EventType;
import pp.commonlib.domain.outbox.OutboxStatus;
import pp.orderservice.outbox.OutboxEvent;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OutboxEventUtils {

    private final ObjectMapper objectMapper;

    public OutboxEvent create(Long eventId, EventType eventType, Object payload) {
        try {
            return OutboxEvent.builder()
                    .eventId(eventId)
                    .eventType(eventType)
                    .payload(objectMapper.writeValueAsString(payload))
                    .status(OutboxStatus.PENDING)
                    .createdAt(Instant.now())
                    .build();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload", e);
        }
    }
}
