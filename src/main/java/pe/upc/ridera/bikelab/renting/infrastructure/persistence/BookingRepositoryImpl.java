package pe.upc.ridera.bikelab.renting.infrastructure.persistence;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;
import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;
import pe.upc.ridera.bikelab.renting.domain.persistence.BookingRepository;
import pe.upc.ridera.bikelab.renting.infrastructure.persistence.jpa.entities.BookingEntity;
import pe.upc.ridera.bikelab.renting.infrastructure.persistence.jpa.repositories.BookingJpaRepository;

@Repository
/**
 * Esta clase implementa el repositorio de reservas utilizando JPA y conversión de entidades.
 */
public class BookingRepositoryImpl implements BookingRepository {

    private static final Collection<BookingState> ACTIVE_STATES = List.of(BookingState.PENDING, BookingState.CONFIRMED,
            BookingState.ACTIVE);

    private final BookingJpaRepository bookingJpaRepository;

    public BookingRepositoryImpl(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = BookingEntity.fromAggregate(booking);
        bookingJpaRepository.save(entity);
        return booking;
    }

    @Override
    public Optional<Booking> findById(UUID bookingId) {
        return bookingJpaRepository.findById(bookingId).map(BookingEntity::toAggregate);
    }

    @Override
    public List<Booking> findByCustomerId(Long customerId) {
        return bookingJpaRepository.findByCustomerIdOrderByStartAtDesc(customerId).stream()
                .map(BookingEntity::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByProviderId(Long providerId) {
        return bookingJpaRepository.findByProviderIdOrderByStartAtDesc(providerId).stream()
                .map(BookingEntity::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsActiveBookingForVehicle(UUID vehicleId, Instant startAt, Instant endAt) {
        return bookingJpaRepository.existsActiveBooking(vehicleId, ACTIVE_STATES, startAt, endAt);
    }

    @Override
    public List<Booking> findByVehicleIdAndStates(UUID vehicleId, List<BookingState> states) {
        return bookingJpaRepository.findByVehicleIdAndStateIn(vehicleId, states).stream()
                .map(BookingEntity::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public long countByState(BookingState state) {
        return bookingJpaRepository.countByState(state);
    }
}
