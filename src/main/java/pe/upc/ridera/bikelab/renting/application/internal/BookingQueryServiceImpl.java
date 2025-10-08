package pe.upc.ridera.bikelab.renting.application.internal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import pe.upc.ridera.bikelab.renting.application.services.BookingQueryService;
import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;
import pe.upc.ridera.bikelab.renting.domain.persistence.BookingRepository;

@Service
/**
 * Esta clase implementa los casos de uso de consulta para obtener reservas por cliente o proveedor.
 */
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingRepository bookingRepository;

    public BookingQueryServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = Objects.requireNonNull(bookingRepository);
    }

    @Override
    public Optional<Booking> findById(UUID bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public List<Booking> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getProviderBookings(Long providerId) {
        return bookingRepository.findByProviderId(providerId);
    }
}
