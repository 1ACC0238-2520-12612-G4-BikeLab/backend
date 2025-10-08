package pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

@Entity
@Table(name = "provider_verifications")
public class ProviderVerificationEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderEntity provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false)
    private ProviderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private ProviderStatus newStatus;

    @Column(name = "actor_id", nullable = false)
    private UUID actorId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ProviderVerificationEntity() {
        // JPA only
    }

    public ProviderVerificationEntity(UUID id, ProviderEntity provider, ProviderStatus oldStatus,
            ProviderStatus newStatus, UUID actorId, String reason, Instant createdAt) {
        this.id = id;
        this.provider = provider;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.actorId = actorId;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public ProviderEntity getProvider() {
        return provider;
    }

    public void setProvider(ProviderEntity provider) {
        this.provider = provider;
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
