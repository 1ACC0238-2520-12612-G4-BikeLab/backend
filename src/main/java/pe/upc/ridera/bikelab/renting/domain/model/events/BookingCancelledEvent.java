package pe.upc.ridera.bikelab.renting.domain.model.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase modela el evento de dominio emitido cuando una reserva es cancelada.
 */
public record BookingCancelledEvent(
        UUID bookingId,
        Instant occurredOn,
        Long customerId,
        UUID vehicleId,
        Instant cancelledAt) implements BookingDomainEvent {
}
