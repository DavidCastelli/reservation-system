--liquibase formatted sql

--changeset david:1
CREATE TABLE reservation (
    reservation_id bigint GENERATED ALWAYS AS IDENTITY,
    name varchar(20) NOT NULL,
    start_time timestamp NOT NULL CONSTRAINT start_time_limit CHECK (start_time > current_date - interval '6 months'),
    end_time timestamp NOT NULL CONSTRAINT end_time_limit CHECK (end_time < current_date + interval '6 months'),
    people int NOT NULL CONSTRAINT positive_people CHECK (people > 0),
    min_people int NOT NULL CONSTRAINT positive_min_people CHECK (min_people > 0),
    max_people int NOT NULL CONSTRAINT positive_max_people CHECK (max_people > 0),
    confirmation_code uuid NOT NULL UNIQUE,
    status status NOT NULL,
    note text,
    reminder text,
    CONSTRAINT valid_start_time CHECK (start_time < end_time),
    CONSTRAINT valid_end_time CHECK (end_time > start_time),
    CONSTRAINT valid_min_people CHECK (min_people < max_people),
    CONSTRAINT valid_max_people CHECK (max_people > min_people),
    CONSTRAINT valid_people CHECK (people BETWEEN min_people AND max_people),
    CONSTRAINT overlapping_reservation EXCLUDE USING GIST (tsrange(start_time, end_time, '[]') WITH &&),
    PRIMARY KEY (reservation_id)
)
--rollback drop table reservation