package pe.upc.ridera.bikelab.renting.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;

/**
 * Esta interfaz agrupa los casos de uso orientados a la consulta de reservas.
 */
public interface BookingQueryService {

    Optional<Booking> findById(UUID bookingId);

    List<Booking> getCustomerBookings(Long customerId);

    List<Booking> getProviderBookings(Long providerId);
}
