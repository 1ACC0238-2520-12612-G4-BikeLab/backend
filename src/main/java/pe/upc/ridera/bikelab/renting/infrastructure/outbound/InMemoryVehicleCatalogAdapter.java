package pe.upc.ridera.bikelab.renting.infrastructure.outbound;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.renting.application.outbound.VehicleCatalogPort;
import pe.upc.ridera.bikelab.renting.infrastructure.config.VehicleCatalogProperties;
import pe.upc.ridera.bikelab.renting.infrastructure.config.VehicleCatalogProperties.VehicleSeed;

@Component
/**
 * Esta clase proporciona un adaptador en memoria que simula la integración con el catálogo de vehículos.
 */
public class InMemoryVehicleCatalogAdapter implements VehicleCatalogPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryVehicleCatalogAdapter.class);

    private final Map<UUID, VehicleSnapshot> catalog = new ConcurrentHashMap<>();
    private final Map<UUID, Reservation> reservations = new ConcurrentHashMap<>();

    public InMemoryVehicleCatalogAdapter(VehicleCatalogProperties properties) {
        Objects.requireNonNull(properties, "properties");
        for (VehicleSeed seed : properties.getSeeds()) {
            if (seed.getId() != null && seed.getProviderId() != null) {
                catalog.put(seed.getId(), new VehicleSnapshot(seed.getId(), seed.getProviderId(),
                        Optional.ofNullable(seed.getHourlyRate()).orElse(BigDecimal.TEN)));
            }
        }
        if (catalog.isEmpty()) {
            seedDefaultVehicles();
        }
    }

    @Override
    public VehicleSnapshot getVehicleSnapshot(UUID vehicleId) {
        return catalog.get(vehicleId);
    }

    @Override
    public boolean isAvailable(UUID vehicleId, Instant startAt, Instant endAt) {
        Objects.requireNonNull(vehicleId, "vehicleId");
        Objects.requireNonNull(startAt, "startAt");
        Objects.requireNonNull(endAt, "endAt");
        if (!catalog.containsKey(vehicleId)) {
            return false;
        }
        Reservation reservation = reservations.get(vehicleId);
        if (reservation == null) {
            return true;
        }
        return reservation.endAt.isBefore(startAt) || reservation.startAt.isAfter(endAt);
    }

    @Override
    public BigDecimal quote(UUID vehicleId, Instant startAt, Instant endAt) {
        VehicleSnapshot snapshot = catalog.get(vehicleId);
        if (snapshot == null) {
            throw new IllegalArgumentException("Vehicle not found: " + vehicleId);
        }
        long minutes = Duration.between(startAt, endAt).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60);
        BigDecimal amount = snapshot.hourlyRate().multiply(BigDecimal.valueOf(hours));
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void reserve(UUID vehicleId, Instant startAt, Instant endAt) {
        reservations.put(vehicleId, new Reservation(startAt, endAt));
        LOGGER.info("Vehicle {} reserved from {} to {}", vehicleId, startAt, endAt);
    }

    @Override
    public void release(UUID vehicleId) {
        reservations.remove(vehicleId);
        LOGGER.info("Vehicle {} released", vehicleId);
    }

    private void seedDefaultVehicles() {
        UUID vehicleOne = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID vehicleTwo = UUID.fromString("22222222-2222-2222-2222-222222222222");
        catalog.put(vehicleOne, new VehicleSnapshot(vehicleOne, 1L, new BigDecimal("8.00")));
        catalog.put(vehicleTwo, new VehicleSnapshot(vehicleTwo, 2L, new BigDecimal("9.50")));
    }

    private record Reservation(Instant startAt, Instant endAt) {
    }
}
