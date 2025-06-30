--liquibase formatted sql

--changeset david:1
CREATE VIEW package_v AS
    SELECT package_id, min_people, max_people, admission_price, start_interval
    FROM package
--rollback drop view package_v
