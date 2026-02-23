create table restaurant_staff(
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants (id),
    role VARCHAR(64) NOT NULL,
    status VARCHAR(64) NOT NULL,
    hired_at TIMESTAMP NOT NULL,
    fired_at TIMESTAMP
);
