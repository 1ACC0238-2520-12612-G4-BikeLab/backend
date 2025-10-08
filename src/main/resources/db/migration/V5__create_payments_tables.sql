CREATE TABLE payment_methods (
    id UUID PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    brand VARCHAR(50) NOT NULL,
    last4 CHAR(4) NOT NULL,
    token_ref VARCHAR(255) NOT NULL,
    is_default BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_payment_methods_customer ON payment_methods (customer_id);

CREATE TABLE charges (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL,
    customer_id BIGINT NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    idempotency_key VARCHAR(255) NOT NULL,
    capture_idempotency_key VARCHAR(255),
    refund_reason TEXT,
    captured_amount NUMERIC(12, 2),
    refunded_amount NUMERIC(12, 2),
    processor_reference VARCHAR(255) NOT NULL,
    CONSTRAINT uk_charges_idempotency UNIQUE (idempotency_key)
);

CREATE INDEX idx_charges_customer ON charges (customer_id);
CREATE INDEX idx_charges_booking ON charges (booking_id);

CREATE TABLE payouts (
    id UUID PRIMARY KEY,
    provider_id BIGINT NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_payouts_provider ON payouts (provider_id);
