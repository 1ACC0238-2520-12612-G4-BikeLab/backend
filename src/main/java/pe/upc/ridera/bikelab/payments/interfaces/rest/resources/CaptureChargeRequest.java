package pe.upc.ridera.bikelab.payments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CaptureChargeRequest")
public record CaptureChargeRequest(@NotBlank String idempotencyKey) {
}
