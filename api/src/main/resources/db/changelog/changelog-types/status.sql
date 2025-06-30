--liquibase formatted sql

--changeset david:1
CREATE TYPE status AS ENUM ('paid', 'teed off', 'no show', 'other')
--rollback drop type status