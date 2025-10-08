package pe.upc.ridera.bikelab.providing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload para enviar los datos KYC del proveedor.
 */
public record SubmitKycRequest(
        @NotBlank String displayName,
        @NotBlank String phone,
        @NotBlank String docType,
        @NotBlank String docNumber) {
}
