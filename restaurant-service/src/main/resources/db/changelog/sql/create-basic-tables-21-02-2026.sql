create table restaurant_brands(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    owner_user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

create table restaurants(
    id BIGSERIAL PRIMARY KEY,
    brand_id BIGINT NOT NULL REFERENCES restaurant_brands (id),
    name VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    phone_number VARCHAR(16) NOT NULL,
    email VARCHAR(320) NOT NULL,
    instagram_profile_link TEXT,
    average_rating NUMERIC(3, 2) DEFAULT 0.00 CHECK (average_rating BETWEEN 0 AND 5),
    rating_count INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

create table restaurant_addresses(
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants (id),
    formatted TEXT NOT NULL,
    location GEOGRAPHY(Point, 4326) NOT NULL,
    country VARCHAR(128) NOT NULL,
    city VARCHAR(128) NOT NULL,
    street VARCHAR(128),
    house VARCHAR(32),
    floor VARCHAR(3),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (id, restaurant_id)
);

create table restaurant_working_hours(
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants (id),
    day_of_week INT CHECK ( day_of_week BETWEEN 1 AND 7),
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    is_active boolean NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (restaurant_id, day_of_week, open_time, close_time)
);

create table restaurant_ratings(
    id BIGSERIAL PRIMARY KEY,
    client_user_id UUID NOT NULL,
    restaurant_id BIGINT REFERENCES restaurants (id),
    rating SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    status VARCHAR(64) NOT NULL DEFAULT 'APPROVED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (client_user_id, restaurant_id)
);

create table restaurant_menus(
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants (id),
    type VARCHAR(64) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

create table menu_items(
    id BIGSERIAL PRIMARY KEY,
    menu_id BIGINT REFERENCES restaurant_menus (id),
    name VARCHAR(128) NOT NULL,
    description TEXT,
    type VARCHAR(128) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    image_url VARCHAR(256),
    weight_grams NUMERIC(6, 2) NOT NULL,
    calories INT NOT NULL,
    preparation_time_minutes INT NOT NULL,
    is_available boolean NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

create table menu_items_discount(
    id BIGSERIAL PRIMARY KEY,
    menu_item_id BIGINT REFERENCES menu_items (id),
    discount_percent INT NOT NULL CHECK ( discount_percent BETWEEN 1 AND 99),
    is_active boolean NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL CHECK (end_time > start_time),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (menu_item_id, start_time)
);

CREATE INDEX idx_restaurant_addresses_location
ON restaurant_addresses USING GIST (location);

CREATE INDEX idx_ratings_restaurant
ON restaurant_ratings (restaurant_id);

CREATE INDEX idx_restaurants_brand_id
ON restaurants (brand_id);

CREATE INDEX idx_restaurant_addresses_restaurant_id
ON restaurant_addresses (restaurant_id);

CREATE INDEX idx_restaurant_working_hours_restaurant_id
ON restaurant_working_hours (restaurant_id);

CREATE INDEX idx_restaurant_ratings_restaurant_id
ON restaurant_ratings (restaurant_id);

CREATE INDEX idx_restaurant_menus_restaurant_id
ON restaurant_menus (restaurant_id);

CREATE INDEX idx_menu_items_menu_id
ON menu_items (menu_id);

CREATE INDEX idx_menu_items_discount_menu_item_id
ON menu_items_discount (menu_item_id);
