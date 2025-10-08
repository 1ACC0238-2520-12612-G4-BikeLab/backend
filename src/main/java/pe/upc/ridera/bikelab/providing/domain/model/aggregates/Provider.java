package pe.upc.ridera.bikelab.providing.domain.model.aggregates;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import pe.upc.ridera.bikelab.providing.domain.exceptions.ProviderStatusException;
import pe.upc.ridera.bikelab.providing.domain.model.entities.ProviderVerification;
import pe.upc.ridera.bikelab.providing.domain.model.events.ProviderApprovedDomainEvent;
import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Agregado raíz que representa a un proveedor dentro de BikeLak.
 */
public class Provider {

    private final UUID id;
    private final UUID userId;
    private ProviderStatus status;
    private String displayName;
    private String phone;
    private String docType;
    private String docNumber;
    private Instant createdAt;
    private Instant updatedAt;
    private final List<ProviderVerification> verifications;

    private Provider(UUID id, UUID userId, ProviderStatus status, String displayName, String phone, String docType,
            String docNumber, Instant createdAt, Instant updatedAt, List<ProviderVerification> verifications) {
        this.id = Objects.requireNonNull(id, "id");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.status = Objects.requireNonNull(status, "status");
        this.displayName = requireText(displayName, "displayName");
        this.phone = requireText(phone, "phone");
        this.docType = requireText(docType, "docType");
        this.docNumber = requireText(docNumber, "docNumber");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
        this.verifications = new ArrayList<>(Objects.requireNonNull(verifications, "verifications"));
    }

    public static Provider requestOnboarding(UUID userId, String displayName, String phone, String docType,
            String docNumber) {
        Instant now = Instant.now();
        return new Provider(UUID.randomUUID(), userId, ProviderStatus.PENDING, displayName, phone, docType, docNumber,
                now, now, new ArrayList<>());
    }

    public static Provider rehydrate(UUID id, UUID userId, ProviderStatus status, String displayName, String phone,
            String docType, String docNumber, Instant createdAt, Instant updatedAt,
            List<ProviderVerification> verifications) {
        return new Provider(id, userId, status, displayName, phone, docType, docNumber, createdAt, updatedAt,
                verifications);
    }

    public ProviderVerification approve(UUID actorId, String reason) {
        if (status == ProviderStatus.APPROVED) {
            throw new ProviderStatusException(status, "aprobar nuevamente");
        }
        ProviderVerification verification = registerTransition(actorId, reason, ProviderStatus.APPROVED);
        return verification;
    }

    public ProviderVerification reject(UUID actorId, String reason) {
        if (status == ProviderStatus.REJECTED) {
            throw new ProviderStatusException(status, "rechazar nuevamente");
        }
        return registerTransition(actorId, reason, ProviderStatus.REJECTED);
    }

    public void submitKyc(String displayName, String phone, String docType, String docNumber) {
        this.displayName = requireText(displayName, "displayName");
        this.phone = requireText(phone, "phone");
        this.docType = requireText(docType, "docType");
        this.docNumber = requireText(docNumber, "docNumber");
        touch();
    }

    private ProviderVerification registerTransition(UUID actorId, String reason, ProviderStatus newStatus) {
        Objects.requireNonNull(actorId, "actorId");
        ProviderStatus previous = this.status;
        this.status = Objects.requireNonNull(newStatus, "newStatus");
        Instant now = Instant.now();
        ProviderVerification verification = ProviderVerification.of(id, previous, newStatus, actorId,
                requireText(reason, "reason"), now);
        this.verifications.add(verification);
        this.updatedAt = now;
        return verification;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public ProviderApprovedDomainEvent toApprovedEvent() {
        if (status != ProviderStatus.APPROVED) {
            throw new ProviderStatusException(status, "emitir evento de aprobación");
        }
        return new ProviderApprovedDomainEvent(id, userId, Instant.now());
    }

    private String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
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

    public List<ProviderVerification> getVerifications() {
        return Collections.unmodifiableList(verifications);
    }

    public boolean isApproved() {
        return status == ProviderStatus.APPROVED;
    }
}
