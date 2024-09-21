CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name  VARCHAR(255)                        NOT NULL,
    email VARCHAR(512)                        NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name        VARCHAR(255)                        NOT NULL,
    description VARCHAR(512)                        NOT NULL,
    owner_id    BIGINT                              NOT NULL,
    available   BOOLEAN                             NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS bookings
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    start     TIMESTAMP                           NOT NULL,
    end_of    TIMESTAMP                           NOT NULL,
    item_id   BIGINT                              NOT NULL,
    booker_id BIGINT                              NOT NULL,
    status    VARCHAR(8)                          NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES items,
    CONSTRAINT fk_booker FOREIGN KEY (booker_id) REFERENCES users,
    CHECK (status IN ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED'))
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    item_id   BIGINT                              NOT NULL,
    author_id BIGINT                              NOT NULL,
    text      VARCHAR(512),
    created   TIMESTAMP                           Not Null,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_item_com FOREIGN KEY (item_id) REFERENCES items,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users
);