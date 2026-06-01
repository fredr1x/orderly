CREATE TABLE outbox_events(
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    event_type VARCHAR(128) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP
);

CREATE INDEX idx_outbox_events_status
ON outbox_events (status) WHERE status = 'PENDING'
