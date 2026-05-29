CREATE TABLE restaurant_orders(
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    user_uuid UUID NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by BIGINT,
    modified_at TIMESTAMP,
    UNIQUE (id, order_id)
);

CREATE TABLE restaurant_order_items(
    id BIGSERIAL PRIMARY KEY,
    restaurant_order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    item_name VARCHAR(128),
    quantity INTEGER CHECK ( quantity > 0 )
);

CREATE INDEX idx_restaurant_orders_status
ON restaurant_orders (status)
