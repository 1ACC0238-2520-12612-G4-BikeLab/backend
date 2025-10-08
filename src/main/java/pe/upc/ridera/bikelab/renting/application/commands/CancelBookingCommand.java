package pe.upc.ridera.bikelab.renting.application.commands;

import java.util.UUID;

/**
 * Esta clase encapsula los datos requeridos para cancelar una reserva creada por un cliente.
 */
public record CancelBookingCommand(
        Long customerId,
        UUID bookingId) {
}
