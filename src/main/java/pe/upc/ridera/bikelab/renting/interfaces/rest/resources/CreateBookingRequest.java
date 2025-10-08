package pe.upc.ridera.bikelab.renting.interfaces.rest.resources;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateBookingRequest")
/**
 * Esta clase modela la solicitud REST utilizada para crear una nueva reserva.
 */
public record CreateBookingRequest(
        @NotNull UUID vehicleId,
        @NotNull @FutureOrPresent Instant startAt,
        @NotNull @Future Instant endAt) {
}
