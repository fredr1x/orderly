CREATE TYPE payment_status AS ENUM(
    'PENDING_PAYMENT',
    'PAYMENT_SUCCEEDED',
    'PAYMENT_FAILED'
    );

CREATE TYPE payment_currency AS ENUM(
    'KZT',
    'USD',
    'RUB',
    'EUR'
    );

CREATE TABLE payments(
                         id BIGSERIAL PRIMARY KEY,
                         order_id BIGINT NOT NULL,
                         user_uuid UUID NOT NULL,
                         status payment_status NOT NULL,
                         currency payment_currency NOT NULL,
                         total_amount NUMERIC(10, 2) NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_order_id
    ON payments(order_id);

CREATE INDEX idx_payments_user_uuid
    ON payments(user_uuid);

CREATE INDEX idx_payments_status
    ON payments(status);
