package pe.upc.ridera.bikelab.providing.domain.model.entities;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Representa el registro histórico de una transición de estado del proveedor.
 */
public final class ProviderVerification {

    private final UUID id;
    private final UUID providerId;
    private final ProviderStatus oldStatus;
    private final ProviderStatus newStatus;
    private final UUID actorId;
    private final String reason;
    private final Instant createdAt;

    private ProviderVerification(UUID id, UUID providerId, ProviderStatus oldStatus, ProviderStatus newStatus,
            UUID actorId, String reason, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.providerId = Objects.requireNonNull(providerId, "providerId");
        this.oldStatus = Objects.requireNonNull(oldStatus, "oldStatus");
        this.newStatus = Objects.requireNonNull(newStatus, "newStatus");
        this.actorId = Objects.requireNonNull(actorId, "actorId");
        this.reason = Objects.requireNonNull(reason, "reason");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public static ProviderVerification of(UUID providerId, ProviderStatus oldStatus, ProviderStatus newStatus,
            UUID actorId, String reason, Instant createdAt) {
        return new ProviderVerification(UUID.randomUUID(), providerId, oldStatus, newStatus, actorId, reason,
                createdAt);
    }

    public static ProviderVerification rehydrate(UUID id, UUID providerId, ProviderStatus oldStatus,
            ProviderStatus newStatus, UUID actorId, String reason, Instant createdAt) {
        return new ProviderVerification(id, providerId, oldStatus, newStatus, actorId, reason, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public ProviderStatus getOldStatus() {
        return oldStatus;
    }

    public ProviderStatus getNewStatus() {
        return newStatus;
    }

    public UUID getActorId() {
        return actorId;
    }

    public String getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
