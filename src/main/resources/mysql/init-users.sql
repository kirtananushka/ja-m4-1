CREATE DATABASE IF NOT EXISTS usersdb;
USE usersdb;

CREATE TABLE IF NOT EXISTS user_entity
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name     VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    role          VARCHAR(255) NOT NULL
);

INSERT INTO user_entity (user_name, user_password, role)
VALUES ('user1@mail.com', '{noop}password', 'USER'),
       ('user2@mail.com', '{bcrypt}$2a$12$s1RzFAScxaWKmKFz6bxJS.D25iDkAaBWitylPCQysx2bHaBNZDarS', 'USER')
;