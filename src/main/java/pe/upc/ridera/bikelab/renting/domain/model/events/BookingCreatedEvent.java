package pe.upc.ridera.bikelab.renting.domain.model.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase modela el evento de dominio emitido cuando una reserva es creada.
 */
public record BookingCreatedEvent(
        UUID bookingId,
        Instant occurredOn,
        Long customerId,
        Long providerId,
        UUID vehicleId,
        Instant startAt,
        Instant endAt,
        BigDecimal authorizedAmount) implements BookingDomainEvent {
}
