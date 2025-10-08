package pe.upc.ridera.bikelab.vehicles.domain.exceptions;

import java.util.UUID;

/**
 * Esta excepción indica que no se encontró un vehículo con el identificador solicitado.
 */ 
public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(UUID id) {
        super("No se encontró el vehículo con id " + id);
    }
}
