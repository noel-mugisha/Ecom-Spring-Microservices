CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE categories
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE products
(
    id                 UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    available_quantity DOUBLE PRECISION NOT NULL DEFAULT 0,
    price              NUMERIC(19, 4) NOT NULL,
    category_id        UUID NOT NULL,
    CONSTRAINT fk_products_on_category
        FOREIGN KEY (category_id)
            REFERENCES categories (id)
            ON DELETE CASCADE
);
-- Index for faster lookups on category_id
CREATE INDEX idx_products_on_category_id ON products (category_id);