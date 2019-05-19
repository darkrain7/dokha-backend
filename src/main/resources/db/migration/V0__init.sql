CREATE SCHEMA dokha;

CREATE TABLE IF NOT EXISTS dokha.role
(
    id    SERIAL PRIMARY KEY NOT NULL,
    name  TEXT UNIQUE        NOT NULL,
    alias TEXT UNIQUE        NOT NULL
);

INSERT INTO dokha.role (name, alias)
VALUES ('Администратор', 'ADMIN');
INSERT INTO dokha.role (name, alias)
VALUES ('Юзер', 'User');

CREATE TABLE IF NOT EXISTS dokha.user
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    login    TEXT UNIQUE           NOT NULL,
    password TEXT                  NOT NULL,
    role_id  INTEGER REFERENCES dokha.role (id)
);

INSERT INTO dokha.user (login, password, role_id)
VALUES ('admin', 'admin', 1);