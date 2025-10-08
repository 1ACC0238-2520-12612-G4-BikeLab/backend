CREATE TABLE IF NOT EXISTS bookings (
    id UUID PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    vehicle_id UUID NOT NULL,
    start_at TIMESTAMPTZ NOT NULL,
    end_at TIMESTAMPTZ NOT NULL,
    state VARCHAR(20) NOT NULL,
    authorized_amount NUMERIC(10, 2) NOT NULL,
    captured_amount NUMERIC(10, 2),
    penalty_amount NUMERIC(10, 2),
    payment_authorization_id VARCHAR(128) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    activated_at TIMESTAMPTZ,
    finished_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_bookings_customer ON bookings(customer_id);
CREATE INDEX IF NOT EXISTS idx_bookings_vehicle ON bookings(vehicle_id);
