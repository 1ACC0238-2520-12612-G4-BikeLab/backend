CREATE TABLE providers (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    doc_type VARCHAR(50) NOT NULL,
    doc_number VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_providers_user_id ON providers (user_id);
CREATE INDEX idx_providers_status ON providers (status);

CREATE TABLE provider_verifications (
    id UUID PRIMARY KEY,
    provider_id UUID NOT NULL,
    old_status VARCHAR(32) NOT NULL,
    new_status VARCHAR(32) NOT NULL,
    actor_id UUID NOT NULL,
    reason TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_provider_verifications_provider
        FOREIGN KEY (provider_id) REFERENCES providers (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_provider_verifications_provider ON provider_verifications (provider_id);
