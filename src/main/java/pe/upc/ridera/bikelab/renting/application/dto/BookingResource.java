package pe.upc.ridera.bikelab.renting.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;

@Schema(name = "Booking")
/**
 * Esta clase representa el recurso expuesto vía API que resume los datos principales de una reserva.
 */
public record BookingResource(
        UUID id,
        Long customerId,
        Long providerId,
        UUID vehicleId,
        Instant startAt,
        Instant endAt,
        Instant activatedAt,
        Instant finishedAt,
        BookingState state,
        BigDecimal authorizedAmount,
        BigDecimal capturedAmount,
        BigDecimal penaltyAmount) {
}
