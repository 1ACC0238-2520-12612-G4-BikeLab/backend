package pe.upc.ridera.bikelab.renting.application.commands;

import java.util.UUID;

/**
 * Esta clase contiene los datos que permiten a un proveedor finalizar un alquiler activo.
 */
public record FinishRentalCommand(
        Long providerId,
        UUID bookingId) {
}
