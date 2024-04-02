CREATE SCHEMA IF NOT EXISTS public_library;

CREATE TABLE IF NOT EXISTS public_library.library
(
    id       BIGINT PRIMARY KEY,
    name     VARCHAR(255) UNIQUE,
    location VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS public_library.library_hours
(
    id            BIGINT PRIMARY KEY,
    day_of_week   VARCHAR(255),
    opening_hours VARCHAR(255),
    closing_hours VARCHAR(255),
    library_id    BIGINT,
    FOREIGN KEY (library_id) REFERENCES public_library.library (id)
);


CREATE TABLE IF NOT EXISTS public_library.books
(
    id               BIGINT PRIMARY KEY,
    title            VARCHAR(255),
    author           VARCHAR(255),
    genre            VARCHAR(255) CHECK (genre IN ('MANGA', 'SCIENCE_FICTION', 'ACADEMIA', 'MEMOIR')),
    publication_date DATE,
    library_id       BIGINT,
    FOREIGN KEY (library_id) REFERENCES public_library.library (id)
);