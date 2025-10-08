package pe.upc.ridera.bikelab.renting.domain.model.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Esta clase representa el evento de dominio que comunica la finalización de un alquiler.
 */
public record RentalFinishedEvent(
        UUID bookingId,
        Instant occurredOn,
        Long providerId,
        UUID vehicleId,
        Instant finishedAt,
        BigDecimal totalCapturedAmount,
        BigDecimal penaltyAmount) implements BookingDomainEvent {
}
