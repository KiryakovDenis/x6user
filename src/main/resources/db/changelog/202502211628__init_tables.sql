CREATE SCHEMA USER_SERVICE;

CREATE TABLE USER_SERVICE.USER (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
)