package pe.upc.ridera.bikelab.payments.domain.model.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido cuando un cargo ha sido autorizado exitosamente.
 */
public record PaymentAuthorizedEvent(UUID chargeId,
                                     UUID bookingId,
                                     Long customerId,
                                     BigDecimal amount,
                                     String currency,
                                     Instant occurredOn) implements PaymentDomainEvent {
}
