package pe.upc.ridera.bikelab.vehicles.domain.model.valueobjects;

/**
 * Esta enumeración representa los estados disponibles para un vehículo dentro del flujo de renting.
 */ 
public enum VehicleStatus {
    AVAILABLE,
    RESERVED,
    IN_SERVICE,
    UNAVAILABLE
}
