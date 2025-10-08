package pe.upc.ridera.bikelab.renting.domain.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;
import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;

/**
 * Esta interfaz define el repositorio de dominio para persistir y recuperar reservas.
 */
public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(UUID bookingId);

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByProviderId(Long providerId);

    boolean existsActiveBookingForVehicle(UUID vehicleId, Instant startAt, Instant endAt);

    List<Booking> findByVehicleIdAndStates(UUID vehicleId, List<BookingState> states);
}
