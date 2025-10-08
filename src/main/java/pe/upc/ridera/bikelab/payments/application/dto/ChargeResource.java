package pe.upc.ridera.bikelab.payments.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;

@Schema(name = "Charge")
public record ChargeResource(UUID id,
                             UUID bookingId,
                             Long customerId,
                             BigDecimal amount,
                             String currency,
                             ChargeStatus status,
                             BigDecimal capturedAmount,
                             BigDecimal refundedAmount,
                             Instant createdAt,
                             Instant updatedAt) {
}
