package pe.upc.ridera.bikelab.providing.application.commands;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Comando para rechazar un proveedor.
 */
public record RejectProviderCommand(
        @NotNull UUID adminId,
        @NotNull UUID providerId,
        @NotBlank String reason) {
}
