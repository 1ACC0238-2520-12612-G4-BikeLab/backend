package pe.upc.ridera.bikelab.providing.application.dto;

import java.time.Instant;
import java.util.UUID;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Representación REST de un registro de verificación de proveedor.
 */
public record ProviderVerificationResource(
        UUID id,
        ProviderStatus oldStatus,
        ProviderStatus newStatus,
        UUID actorId,
        String reason,
        Instant createdAt) {
}
