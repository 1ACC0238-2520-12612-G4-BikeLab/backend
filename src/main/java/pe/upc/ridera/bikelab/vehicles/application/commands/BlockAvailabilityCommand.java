package pe.upc.ridera.bikelab.vehicles.application.commands;

import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase describe la acción de bloquear la disponibilidad de un vehículo durante un intervalo específico.
 */ 
public record BlockAvailabilityCommand(
        UUID requesterId,
        UUID vehicleId,
        Instant startAt,
        Instant endAt) {
}
