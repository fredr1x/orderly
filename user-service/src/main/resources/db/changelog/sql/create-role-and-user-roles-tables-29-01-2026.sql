create table roles(
    id      BIGSERIAL PRIMARY KEY,
    value   VARCHAR(256) NOT NULL UNIQUE
);

create table user_roles(
    user_id     BIGINT REFERENCES users(id),
    role_id     BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);
