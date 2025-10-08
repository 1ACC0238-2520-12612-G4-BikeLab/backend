package pe.upc.ridera.bikelab.renting.application.commands;

import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase encapsula la información necesaria para que un cliente cree una nueva reserva.
 */
public record CreateBookingCommand(
        Long customerId,
        UUID vehicleId,
        Instant startAt,
        Instant endAt) {
}
