package pe.upc.ridera.bikelab.vehicles.domain.model.entities;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Esta entidad representa un intervalo de tiempo en el que un vehículo no puede ser reservado.
 */ 
public class AvailabilitySlot {

    private final UUID id;
    private final Instant startAt;
    private final Instant endAt;

    public AvailabilitySlot(UUID id, Instant startAt, Instant endAt) {
        if (startAt == null || endAt == null || !endAt.isAfter(startAt)) {
            throw new IllegalArgumentException("El rango de tiempo del bloqueo es inválido");
        }
        this.id = Objects.requireNonNull(id, "id");
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public UUID getId() {
        return id;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public boolean overlaps(Instant otherStart, Instant otherEnd) {
        Objects.requireNonNull(otherStart, "otherStart");
        Objects.requireNonNull(otherEnd, "otherEnd");
        if (!otherEnd.isAfter(otherStart)) {
            return true;
        }
        return otherStart.isBefore(endAt) && otherEnd.isAfter(startAt);
    }
}
