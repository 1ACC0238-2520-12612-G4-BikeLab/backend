package pe.upc.ridera.bikelab.vehicles.application.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase expone la representación de un bloqueo de disponibilidad para respuestas REST.
 */ 
public record AvailabilitySlotResource(
        UUID id,
        Instant startAt,
        Instant endAt) {
}
