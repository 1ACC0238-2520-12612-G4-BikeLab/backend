package pe.upc.ridera.bikelab.providing.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Representación del agregado Provider.
 */
public record ProviderResource(
        UUID id,
        UUID userId,
        ProviderStatus status,
        String displayName,
        String phone,
        String docType,
        String docNumber,
        Instant createdAt,
        Instant updatedAt,
        List<ProviderVerificationResource> verifications) {
}
