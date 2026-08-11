-- init Schema for PostgresSQL
CREATE TABLE IF NOT EXISTS people (
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);
insert into people (first_name, last_name) values ('abc 1', "last name 1");
insert into people (first_name, last_name) values ('abc 2', "last name 2");
insert into people (first_name, last_name) values ('abc 3', "last name 3");
insert into people (first_name, last_name) values ('abc 4', "last name 4");

CREATE TABLE IF NOT EXISTS product (
    name VARCHAR(100) NOT NULL,
    value VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS record_db (
    username VARCHAR(100) NOT NULL,
    id BIGINT NOT NULL,
    amount NUMERIC(18, 2) NOT NULL
);