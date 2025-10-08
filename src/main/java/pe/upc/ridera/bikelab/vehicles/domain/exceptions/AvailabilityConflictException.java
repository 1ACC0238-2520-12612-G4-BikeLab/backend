package pe.upc.ridera.bikelab.vehicles.domain.exceptions;

/**
 * Esta excepción se lanza cuando se intenta bloquear disponibilidad que se superpone con una existente.
 */ 
public class AvailabilityConflictException extends VehicleDomainException {

    public AvailabilityConflictException() {
        super("El vehículo ya tiene un bloqueo para el rango solicitado");
    }
}
