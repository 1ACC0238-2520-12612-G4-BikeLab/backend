package pe.upc.ridera.bikelab.vehicles.domain.exceptions;

import java.util.UUID;

/**
 * Esta excepción se utiliza cuando un usuario intenta operar sobre un vehículo que no le pertenece.
 */ 
public class UnauthorizedVehicleAccessException extends VehicleDomainException {

    public UnauthorizedVehicleAccessException(UUID ownerId) {
        super("El usuario " + ownerId + " no está autorizado a operar este vehículo");
    }
}
