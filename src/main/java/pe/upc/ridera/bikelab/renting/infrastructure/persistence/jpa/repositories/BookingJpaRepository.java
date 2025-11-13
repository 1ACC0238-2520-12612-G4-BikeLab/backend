package pe.upc.ridera.bikelab.renting.infrastructure.persistence.jpa.repositories;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;
import pe.upc.ridera.bikelab.renting.infrastructure.persistence.jpa.entities.BookingEntity;

/**
 * Esta interfaz extiende JPA para acceder a los datos persistidos de reservas.
 */
public interface BookingJpaRepository extends JpaRepository<BookingEntity, UUID> {

    List<BookingEntity> findByCustomerIdOrderByStartAtDesc(Long customerId);

    List<BookingEntity> findByProviderIdOrderByStartAtDesc(Long providerId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM BookingEntity b " +
            "WHERE b.vehicleId = :vehicleId AND b.state IN :states " +
            "AND ((b.startAt < :endAt AND b.endAt > :startAt))")
    boolean existsActiveBooking(@Param("vehicleId") UUID vehicleId,
                                @Param("states") Collection<BookingState> states,
                                @Param("startAt") Instant startAt,
                                @Param("endAt") Instant endAt);

    List<BookingEntity> findByVehicleIdAndStateIn(UUID vehicleId, Collection<BookingState> states);

    long countByState(BookingState state);
}
