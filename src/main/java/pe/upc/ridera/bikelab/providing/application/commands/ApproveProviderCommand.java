package pe.upc.ridera.bikelab.providing.application.commands;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Comando para aprobar un proveedor.
 */
public record ApproveProviderCommand(
        @NotNull UUID adminId,
        @NotNull UUID providerId,
        @NotBlank String reason) {
}
