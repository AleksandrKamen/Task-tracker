CREATE SCHEMA IF NOT EXISTS app_data;

CREATE TABLE app_data.users
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(128) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    role  VARCHAR(32) not null
);

CREATE TABLE app_data.tasks
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    iscomplited boolean not null,
    created_at TIMESTAMP not null,
    completed_at TIMESTAMP,
    id_user BIGINT NOT NULL REFERENCES app_data.users(id)
);



