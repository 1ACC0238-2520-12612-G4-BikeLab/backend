package pe.upc.ridera.bikelab.vehicles.application.internal;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.vehicles.application.dto.VehicleResource;
import pe.upc.ridera.bikelab.vehicles.application.internal.mappers.VehicleMapper;
import pe.upc.ridera.bikelab.vehicles.application.services.VehicleQueryService;
import pe.upc.ridera.bikelab.vehicles.domain.exceptions.VehicleNotFoundException;
import pe.upc.ridera.bikelab.vehicles.domain.model.aggregates.Vehicle;
import pe.upc.ridera.bikelab.vehicles.domain.persistence.VehicleRepository;

/**
 * Esta clase implementa las consultas para recuperar vehículos y búsquedas geográficas sencillas.
 */ 
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private static final double EARTH_DEGREE_KM = 111.0d;

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper mapper;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository, VehicleMapper mapper) {
        this.vehicleRepository = vehicleRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResource getVehicle(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new VehicleNotFoundException(vehicleId));
        return mapper.toResource(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResource> getOwnVehicles(UUID ownerId) {
        return vehicleRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResource> listAll() {
        return vehicleRepository.findAll().stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResource> searchNearby(double latitude, double longitude, double radiusKm) {
        double delta = radiusKm / EARTH_DEGREE_KM;
        double minLat = latitude - delta;
        double maxLat = latitude + delta;
        double minLng = longitude - delta;
        double maxLng = longitude + delta;
        return vehicleRepository.searchByBoundingBox(minLat, maxLat, minLng, maxLng).stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }
}
