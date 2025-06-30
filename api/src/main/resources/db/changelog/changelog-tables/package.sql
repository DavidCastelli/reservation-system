--liquibase formatted sql

--changeset david:1
CREATE TABLE package (
    package_id bigint GENERATED ALWAYS AS IDENTITY,
    min_people int NOT NULL CONSTRAINT positive_min_people CHECK (min_people > 0),
    max_people int NOT NULL CONSTRAINT positive_max_people CHECK (max_people > 0),
    admission_price numeric(12, 2) NOT NULL CONSTRAINT positive_or_zero_admission_price CHECK (admission_price >= 0),
    start_interval int NOT NULL CONSTRAINT positive_start_interval CHECK (start_interval > 0),
    CONSTRAINT valid_min_people CHECK (min_people < max_people),
    CONSTRAINT valid_max_people CHECK (max_people > min_people),
    CONSTRAINT overlapping_people EXCLUDE USING GIST (int4range(min_people, max_people, '[]') WITH &&),
    PRIMARY KEY (package_id)
)
--rollback drop table package