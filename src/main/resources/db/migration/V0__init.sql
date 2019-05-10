CREATE SCHEMA dokha;

CREATE TABLE IF NOT EXISTS dokha.user (
  id          BIGSERIAL PRIMARY KEY  NOT NULL,
  first_name  TEXT                   NOT NULL,
  last_name   TEXT                   NOT NULL
);
