package pe.upc.ridera.bikelab.renting.domain.model.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase abstrae el comportamiento común de los eventos de dominio del agregado Booking.
 */
public interface BookingDomainEvent {

    UUID bookingId();

    Instant occurredOn();
}
