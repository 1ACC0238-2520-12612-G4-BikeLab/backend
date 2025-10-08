package pe.upc.ridera.bikelab.vehicles.domain.exceptions;

/**
 * Esta excepción representa problemas de negocio relacionados al agregado Vehicle.
 */ 
public class VehicleDomainException extends RuntimeException {

    public VehicleDomainException(String message) {
        super(message);
    }
}
