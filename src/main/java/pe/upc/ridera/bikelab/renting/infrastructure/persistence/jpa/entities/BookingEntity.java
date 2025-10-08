package pe.upc.ridera.bikelab.renting.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;
import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;

@Entity
@Table(name = "bookings")
/**
 * Esta clase define la entidad JPA que persiste la información de una reserva.
 */
public class BookingEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private BookingState state;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "authorized_amount", nullable = false)
    private BigDecimal authorizedAmount;

    @Column(name = "captured_amount")
    private BigDecimal capturedAmount;

    @Column(name = "penalty_amount")
    private BigDecimal penaltyAmount;

    @Column(name = "payment_authorization_id", nullable = false)
    private String paymentAuthorizationId;

    public BookingEntity() {
    }

    public static BookingEntity fromAggregate(Booking booking) {
        BookingEntity entity = new BookingEntity();
        entity.id = booking.getId();
        entity.customerId = booking.getCustomerId();
        entity.providerId = booking.getProviderId();
        entity.vehicleId = booking.getVehicleId();
        entity.startAt = booking.getStartAt();
        entity.endAt = booking.getEndAt();
        entity.state = booking.getState();
        entity.createdAt = booking.getCreatedAt();
        entity.updatedAt = booking.getUpdatedAt();
        entity.activatedAt = booking.getActivatedAt();
        entity.finishedAt = booking.getFinishedAt();
        entity.authorizedAmount = booking.getAuthorizedAmount();
        entity.capturedAmount = booking.getCapturedAmount();
        entity.penaltyAmount = booking.getPenaltyAmount();
        entity.paymentAuthorizationId = booking.getPaymentAuthorizationId();
        return entity;
    }

    public Booking toAggregate() {
        return Booking.restore(id, customerId, providerId, vehicleId, startAt, endAt, state, createdAt, updatedAt, activatedAt,
                finishedAt, authorizedAmount, capturedAmount, penaltyAmount, paymentAuthorizationId);
    }

    public UUID getId() {
        return id;
    }
}
