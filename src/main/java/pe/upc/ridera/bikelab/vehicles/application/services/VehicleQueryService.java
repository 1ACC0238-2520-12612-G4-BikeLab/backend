package pe.upc.ridera.bikelab.vehicles.application.services;

import java.util.List;
import java.util.UUID;

import pe.upc.ridera.bikelab.vehicles.application.dto.VehicleResource;

/**
 * Esta interfaz agrupa las consultas disponibles sobre el catálogo de vehículos.
 */ 
public interface VehicleQueryService {

    VehicleResource getVehicle(UUID vehicleId);

    List<VehicleResource> getOwnVehicles(UUID ownerId);

    List<VehicleResource> listAll();

    List<VehicleResource> searchNearby(double latitude, double longitude, double radiusKm);
}
