package pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

@Entity
@Table(name = "providers", uniqueConstraints = {
        @UniqueConstraint(name = "uk_providers_user_id", columnNames = "user_id")
})
public class ProviderEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProviderStatus status;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "doc_type", nullable = false)
    private String docType;

    @Column(name = "doc_number", nullable = false)
    private String docNumber;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProviderVerificationEntity> verifications = new ArrayList<>();

    protected ProviderEntity() {
        // JPA only
    }

    public ProviderEntity(UUID id, UUID userId, ProviderStatus status, String displayName, String phone, String docType,
            String docNumber, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.displayName = displayName;
        this.phone = phone;
        this.docType = docType;
        this.docNumber = docNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public ProviderStatus getStatus() {
        return status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<ProviderVerificationEntity> getVerifications() {
        return verifications;
    }

    public void setVerifications(List<ProviderVerificationEntity> verifications) {
        this.verifications = verifications;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
