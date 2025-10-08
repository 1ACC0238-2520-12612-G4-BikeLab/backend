package pe.upc.ridera.bikelab.providing.application.commands;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Comando para registrar o actualizar la información KYC del proveedor.
 */
public record SubmitKycCommand(
        @NotNull UUID userId,
        @NotBlank String displayName,
        @NotBlank String phone,
        @NotBlank String docType,
        @NotBlank String docNumber) {
}
