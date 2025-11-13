package pe.upc.ridera.bikelab.vehicles.domain.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.vehicles.domain.model.aggregates.Vehicle;
import pe.upc.ridera.bikelab.vehicles.domain.model.valueobjects.VehicleStatus;

/**
 * Esta interfaz define las operaciones de persistencia del agregado Vehicle.
 */ 
public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(UUID id);

    List<Vehicle> findByOwnerId(UUID ownerId);

    List<Vehicle> searchByBoundingBox(double minLat, double maxLat, double minLng, double maxLng);

    List<Vehicle> findAll();

    long count();

    long countByStatus(VehicleStatus status);
}
