package pe.upc.ridera.bikelab.providing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload para aprobar o rechazar un proveedor.
 */
public record ProviderDecisionRequest(@NotBlank String reason) {
}
