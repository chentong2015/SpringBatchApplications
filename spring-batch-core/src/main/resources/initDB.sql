-- Init Schema for PostgresSQL
CREATE TABLE IF NOT EXISTS people (
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS record_db (
    username VARCHAR(100) NOT NULL,
    id BIGINT NOT NULL,
    amount NUMERIC(18, 2) NOT NULL
);