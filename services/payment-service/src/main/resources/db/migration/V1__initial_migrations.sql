-- Enable UUID generator extension
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Payment table
CREATE TABLE payments
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount         NUMERIC(19, 4) NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    customer_id    VARCHAR(255) NOT NULL,
    order_id       UUID NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ
);

-- Useful index for queries by order_id
CREATE INDEX idx_payments_order_id ON payments (order_id);
CREATE INDEX idx_payments_customer_id ON payments (customer_id);
CREATE INDEX idx_payments_payment_method ON payments (payment_method);
