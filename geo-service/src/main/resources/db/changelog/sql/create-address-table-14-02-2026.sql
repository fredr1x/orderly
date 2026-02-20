CREATE TABLE addresses(
    id            BIGSERIAL PRIMARY KEY,
    formatted     TEXT NOT NULL,
    location      GEOGRAPHY(Point, 4326) NOT NULL,
    country       VARCHAR(128),
    province      VARCHAR(128),
    locality      VARCHAR(128),
    street        VARCHAR(128),
    house         VARCHAR(128),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_addresses_location ON addresses USING GIST(location);
