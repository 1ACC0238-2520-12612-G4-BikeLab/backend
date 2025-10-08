package pe.upc.ridera.bikelab.payments.domain.model.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido cuando se registra la devolución de un cargo capturado.
 */
public record PaymentRefundedEvent(UUID chargeId,
                                   UUID bookingId,
                                   BigDecimal amount,
                                   String reason,
                                   Instant occurredOn) implements PaymentDomainEvent {
}
