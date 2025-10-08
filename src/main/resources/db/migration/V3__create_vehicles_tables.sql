CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    hourly_price NUMERIC(10, 2) NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lng DOUBLE PRECISION NOT NULL,
    rating_avg NUMERIC(4, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_vehicles_owner ON vehicles(owner_id);

CREATE TABLE vehicle_availability (
    id UUID PRIMARY KEY,
    vehicle_id UUID NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
    start_at TIMESTAMP WITH TIME ZONE NOT NULL,
    end_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uq_vehicle_slot UNIQUE (vehicle_id, start_at, end_at)
);

CREATE INDEX idx_vehicle_availability_vehicle ON vehicle_availability(vehicle_id);
