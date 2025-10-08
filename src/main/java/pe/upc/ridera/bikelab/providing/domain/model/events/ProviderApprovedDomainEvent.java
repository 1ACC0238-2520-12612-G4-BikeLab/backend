package pe.upc.ridera.bikelab.providing.domain.model.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de dominio emitido cuando un proveedor es aprobado.
 */
public record ProviderApprovedDomainEvent(UUID providerId, UUID userId, Instant occurredOn) {
}
