package pe.upc.ridera.bikelab.payments.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AuthorizeChargeRequest")
public record AuthorizeChargeRequest(@NotNull UUID bookingId,
                                     @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
                                     @NotBlank String currency,
                                     @NotBlank String idempotencyKey,
                                     UUID paymentMethodId) {
}
