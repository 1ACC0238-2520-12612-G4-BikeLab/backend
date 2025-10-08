package pe.upc.ridera.bikelab.vehicles.interfaces.rest.resources;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

/**
 * Esta clase modela el cuerpo de la petición para bloquear la disponibilidad de un vehículo.
 */ 
public record BlockAvailabilityRequest(
        @NotNull Instant startAt,
        @NotNull Instant endAt) {
}
