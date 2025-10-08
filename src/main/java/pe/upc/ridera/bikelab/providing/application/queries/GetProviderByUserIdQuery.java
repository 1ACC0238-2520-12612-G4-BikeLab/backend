package pe.upc.ridera.bikelab.providing.application.queries;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Consulta para obtener el proveedor asociado a un usuario.
 */
public record GetProviderByUserIdQuery(@NotNull UUID userId) {
}
