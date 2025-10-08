package pe.upc.ridera.bikelab.providing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload para iniciar el proceso de onboarding de un proveedor.
 */
public record RequestOnboardingRequest(
        @NotBlank String displayName,
        @NotBlank String phone,
        @NotBlank String docType,
        @NotBlank String docNumber) {
}
