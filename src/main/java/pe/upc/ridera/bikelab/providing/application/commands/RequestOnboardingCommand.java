package pe.upc.ridera.bikelab.providing.application.commands;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Comando para iniciar el onboarding de un proveedor.
 */
public record RequestOnboardingCommand(
        @NotNull UUID userId,
        @NotBlank String displayName,
        @NotBlank String phone,
        @NotBlank String docType,
        @NotBlank String docNumber) {
}
