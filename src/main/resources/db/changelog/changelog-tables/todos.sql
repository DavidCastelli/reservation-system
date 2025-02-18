--liquibase formatted sql

--changeset test:1
CREATE TABLE todo (todoId INT NOT NULL, PRIMARY KEY (todoId))