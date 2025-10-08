package pe.upc.ridera.bikelab.payments.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.PayoutStatus;

@Schema(name = "Payout")
public record PayoutResource(UUID id,
                             Long providerId,
                             BigDecimal amount,
                             PayoutStatus status,
                             Instant createdAt,
                             Instant updatedAt) {
}
