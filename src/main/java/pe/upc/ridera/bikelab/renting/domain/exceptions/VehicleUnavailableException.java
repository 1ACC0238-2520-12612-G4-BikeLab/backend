package pe.upc.ridera.bikelab.renting.domain.exceptions;

import java.util.UUID;

/**
 * Esta clase describe la condición de un vehículo no disponible para una reserva dada.
 */
public class VehicleUnavailableException extends RuntimeException {

    public VehicleUnavailableException(UUID vehicleId) {
        super("Vehicle " + vehicleId + " is not available for the requested period");
    }
}
