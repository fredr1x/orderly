create table users(
    id                  BIGSERIAL PRIMARY KEY,
    keycloak_id         UUID NOT NULL,
    email               VARCHAR(254) NOT NULL UNIQUE,
    first_name          VARCHAR(128) NOT NULL,
    last_name           VARCHAR(128) NOT NULL,
    is_active           BOOLEAN NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    avatar_photo_url    TEXT
);

create table user_addresses (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES users(id),
    country         VARCHAR(64) NOT NULL,
    city            VARCHAR(128) NOT NULL,
    street          VARCHAR(128),
    house           VARCHAR(32),
    apartment       VARCHAR(32),
    location        GEOGRAPHY(Point, 4326),
    comment         VARCHAR(255),
    is_default      BOOLEAN NOT NULL DEFAULT false,
    is_active       BOOLEAN NOT NULL DEFAULT true,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP
);

create index idx_user_addresses_location
on user_addresses
using GIST(location);
