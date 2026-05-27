CREATE TYPE order_status AS ENUM (
    'PENDING_PAYMENT',
    'PAYMENT_FAILED',
    'AWAITING_CONFIRMATION',
    'CONFIRMED',
    'PREPARING',
    'READY',
    'DELIVERING',
    'COMPLETED',
    'CANCELLED'
    );

CREATE TABLE orders
(
    id            BIGSERIAL PRIMARY KEY,
    user_uuid     UUID           NOT NULL,
    restaurant_id BIGINT         NOT NULL,
    address       TEXT           NOT NULL,
    status        order_status   NOT NULL DEFAULT 'PENDING_PAYMENT',
    total_amount  NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_user_uuid ON orders (user_uuid);
CREATE INDEX idx_orders_status ON orders (status);

CREATE TABLE order_items
(
    id                  BIGSERIAL PRIMARY KEY,
    order_id            BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    item_id             BIGINT         NOT NULL,
    item_name_snapshot  TEXT           NOT NULL,
    item_price_snapshot NUMERIC(10, 2) NOT NULL CHECK (item_price_snapshot >= 0),
    quantity            INTEGER        NOT NULL CHECK (quantity > 0),
    UNIQUE (order_id, item_id)
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
