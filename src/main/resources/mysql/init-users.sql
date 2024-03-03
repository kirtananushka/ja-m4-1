CREATE DATABASE IF NOT EXISTS usersdb;
USE usersdb;

CREATE TABLE IF NOT EXISTS user_entity
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name     VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_granted_authority
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(255) NOT NULL,
    user_id   BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_entity (id)
);

INSERT INTO user_entity (user_name, user_password)
VALUES ('admin@mail.com', '{bcrypt}$2a$12$s1RzFAScxaWKmKFz6bxJS.D25iDkAaBWitylPCQysx2bHaBNZDarS'),
       ('user@mail.com', '{noop}password'),
       ('admin.user@mail.com', '{bcrypt}$2a$12$s1RzFAScxaWKmKFz6bxJS.D25iDkAaBWitylPCQysx2bHaBNZDarS');

INSERT INTO user_granted_authority (authority, user_id)
VALUES ('VIEW_ADMIN', 1),
       ('VIEW_INFO', 2),
       ('VIEW_ADMIN', 3),
       ('VIEW_INFO', 3);
