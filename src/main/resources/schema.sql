CREATE TABLE IF NOT EXISTS warehouse (
    id         BIGSERIAL PRIMARY KEY,
    code       VARCHAR(16)  NOT NULL UNIQUE,
    name       VARCHAR(100) NOT NULL,
    city       VARCHAR(100) NOT NULL,
    surface_m2 INTEGER      NOT NULL
);
