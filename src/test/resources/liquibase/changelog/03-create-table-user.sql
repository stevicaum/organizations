--liquibase formatted sql
--changeset Stevica:03
CREATE TABLE user_table (id bigint NOT NULL, username varchar(255) NOT NULL);