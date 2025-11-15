CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE orders
(
    id           UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    reference    VARCHAR(255) NOT NULL UNIQUE,
    total_amount NUMERIC(19, 4) NOT NULL,
    payment_method VARCHAR(100),
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ
);
CREATE INDEX idx_orders_reference ON orders (reference);


CREATE TABLE order_lines
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quantity   DOUBLE PRECISION NOT NULL CHECK (quantity > 0),
    product_id UUID NOT NULL,
    order_id   UUID NOT NULL,

    CONSTRAINT fk_order_lines_on_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_order_lines_on_order_id ON order_lines (order_id);
CREATE INDEX idx_order_lines_on_product_id ON order_lines (product_id);