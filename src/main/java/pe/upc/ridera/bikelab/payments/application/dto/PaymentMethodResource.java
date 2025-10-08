package pe.upc.ridera.bikelab.payments.application.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PaymentMethod")
public record PaymentMethodResource(UUID id,
                                    String brand,
                                    String last4,
                                    boolean isDefault,
                                    Instant createdAt) {
}
