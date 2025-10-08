package pe.upc.ridera.bikelab.payments.domain.model.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido cuando un cargo autorizado ha sido capturado.
 */
public record PaymentCapturedEvent(UUID chargeId,
                                   UUID bookingId,
                                   BigDecimal amount,
                                   Instant occurredOn) implements PaymentDomainEvent {
}
