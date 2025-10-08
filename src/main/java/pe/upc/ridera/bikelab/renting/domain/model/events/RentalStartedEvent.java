package pe.upc.ridera.bikelab.renting.domain.model.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase representa el evento de dominio que indica el inicio de un alquiler.
 */
public record RentalStartedEvent(
        UUID bookingId,
        Instant occurredOn,
        Long providerId,
        UUID vehicleId,
        Instant startedAt) implements BookingDomainEvent {
}
