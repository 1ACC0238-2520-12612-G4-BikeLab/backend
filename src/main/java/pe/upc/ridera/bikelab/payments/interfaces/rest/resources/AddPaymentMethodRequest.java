package pe.upc.ridera.bikelab.payments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "AddPaymentMethodRequest")
public record AddPaymentMethodRequest(@NotBlank String tokenRef,
                                      @NotBlank String brand,
                                      @Pattern(regexp = "\\d{4}") String last4,
                                      boolean makeDefault) {
}
