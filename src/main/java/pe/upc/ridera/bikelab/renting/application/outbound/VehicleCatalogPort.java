package pe.upc.ridera.bikelab.renting.application.outbound;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Esta interfaz define las operaciones necesarias para consultar y actualizar la disponibilidad de los vehículos.
 */
public interface VehicleCatalogPort {

    VehicleSnapshot getVehicleSnapshot(UUID vehicleId);

    boolean isAvailable(UUID vehicleId, Instant startAt, Instant endAt);

    BigDecimal quote(UUID vehicleId, Instant startAt, Instant endAt);

    void reserve(UUID vehicleId, Instant startAt, Instant endAt);

    void release(UUID vehicleId);

    record VehicleSnapshot(UUID vehicleId, Long providerId, BigDecimal hourlyRate) {
    }
}
