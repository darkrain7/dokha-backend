
CREATE TABLE IF NOT EXISTS dokha.s_store
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    name     TEXT                  NOT NULL,
    location TEXT                  NOT NULL
);

CREATE TABLE IF NOT EXISTS dokha.s_personal_position
(
    id   BIGSERIAL PRIMARY KEY NOT NULL,
    name TEXT                  NOT NULL
);

CREATE TABLE IF NOT EXISTS dokha.s_personal
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    first_name  TEXT                  NOT NULL,
    last_name   TEXT                  NOT NULL,
    age         INTEGER               NOT NULL,
    position_id BIGINT REFERENCES dokha.s_personal_position (id)
);

CREATE TABLE IF NOT EXISTS dokha.s_place_reservation
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    description TEXT,
    store_id    BIGINT REFERENCES dokha.s_store (id)
);

CREATE TABLE IF NOT EXISTS dokha.reserve
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    place_id BIGINT REFERENCES dokha.s_place_reservation (id) NOT NULL ,
    user_id  BIGINT REFERENCES dokha.user (id) NOT NULL ,
    reserve_time TIMESTAMP NOT NULL
);





