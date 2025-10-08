package pe.upc.ridera.bikelab.renting.application.commands;

import java.util.UUID;

/**
 * Esta clase contiene los parámetros utilizados por un proveedor para iniciar un alquiler confirmado.
 */
public record StartRentalCommand(
        Long providerId,
        UUID bookingId) {
}
