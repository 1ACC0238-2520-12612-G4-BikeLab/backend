package pe.upc.ridera.bikelab.vehicles.infrastructure.persistence.jpa.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Esta entidad JPA conserva los bloqueos de disponibilidad asociados a cada vehículo.
 */ 
@Entity
@Table(name = "vehicle_availability")
public class VehicleAvailabilityEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    public VehicleAvailabilityEntity() {
    }

    public VehicleAvailabilityEntity(UUID id, VehicleEntity vehicle, Instant startAt, Instant endAt) {
        this.id = id;
        this.vehicle = vehicle;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public UUID getId() {
        return id;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }
}
