package pe.upc.ridera.bikelab.renting.domain.model.aggregates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import pe.upc.ridera.bikelab.renting.domain.exceptions.BookingDomainException;
import pe.upc.ridera.bikelab.renting.domain.model.events.BookingCancelledEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.BookingCreatedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.BookingDomainEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.RentalFinishedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.RentalStartedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;

/**
 * Esta clase representa el agregado Booking encargado de gestionar el ciclo de vida de una renta, incluyendo sus invariantes, montos y eventos de dominio.
 */
public class Booking {

    private static final Duration ALLOWED_EARLY_START_WINDOW = Duration.ofMinutes(15);
    private static final BigDecimal PENALTY_PER_HOUR = new BigDecimal("12.50");

    private final UUID id;
    private final Long customerId;
    private final Long providerId;
    private final UUID vehicleId;
    private final Instant startAt;
    private final Instant endAt;
    private BookingState state;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant activatedAt;
    private Instant finishedAt;
    private final BigDecimal authorizedAmount;
    private BigDecimal capturedAmount;
    private BigDecimal penaltyAmount;
    private final String paymentAuthorizationId;

    private final transient List<BookingDomainEvent> domainEvents = new ArrayList<>();

    private Booking(UUID id,
                    Long customerId,
                    Long providerId,
                    UUID vehicleId,
                    Instant startAt,
                    Instant endAt,
                    BookingState state,
                    Instant createdAt,
                    Instant updatedAt,
                    Instant activatedAt,
                    Instant finishedAt,
                    BigDecimal authorizedAmount,
                    BigDecimal capturedAmount,
                    BigDecimal penaltyAmount,
                    String paymentAuthorizationId) {
        this.id = Objects.requireNonNull(id, "id");
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        this.providerId = Objects.requireNonNull(providerId, "providerId");
        this.vehicleId = Objects.requireNonNull(vehicleId, "vehicleId");
        this.startAt = Objects.requireNonNull(startAt, "startAt");
        this.endAt = Objects.requireNonNull(endAt, "endAt");
        this.state = Objects.requireNonNull(state, "state");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
        this.activatedAt = activatedAt;
        this.finishedAt = finishedAt;
        this.authorizedAmount = Objects.requireNonNull(authorizedAmount, "authorizedAmount");
        this.capturedAmount = capturedAmount;
        this.penaltyAmount = penaltyAmount == null ? BigDecimal.ZERO : penaltyAmount;
        this.paymentAuthorizationId = Objects.requireNonNull(paymentAuthorizationId, "paymentAuthorizationId");
    }

    public static Booking createWithId(UUID bookingId,
                                       Long customerId,
                                       Long providerId,
                                       UUID vehicleId,
                                       Instant startAt,
                                       Instant endAt,
                                       BigDecimal authorizedAmount,
                                       String paymentAuthorizationId,
                                       Instant createdAt) {
        if (!startAt.isBefore(endAt)) {
            throw new BookingDomainException("Start time must be before end time");
        }
        Booking booking = new Booking(bookingId,
                customerId,
                providerId,
                vehicleId,
                startAt,
                endAt,
                BookingState.CONFIRMED,
                createdAt,
                createdAt,
                null,
                null,
                authorizedAmount.setScale(2, RoundingMode.HALF_UP),
                null,
                BigDecimal.ZERO,
                paymentAuthorizationId);
        booking.domainEvents.add(new BookingCreatedEvent(booking.id, createdAt, customerId, providerId, vehicleId, startAt, endAt,
                booking.authorizedAmount));
        return booking;
    }

    public static Booking create(Long customerId,
                                 Long providerId,
                                 UUID vehicleId,
                                 Instant startAt,
                                 Instant endAt,
                                 BigDecimal authorizedAmount,
                                 String paymentAuthorizationId,
                                 Instant createdAt) {
        return createWithId(UUID.randomUUID(), customerId, providerId, vehicleId, startAt, endAt, authorizedAmount,
                paymentAuthorizationId, createdAt);
    }

    public static Booking restore(UUID id,
                                  Long customerId,
                                  Long providerId,
                                  UUID vehicleId,
                                  Instant startAt,
                                  Instant endAt,
                                  BookingState state,
                                  Instant createdAt,
                                  Instant updatedAt,
                                  Instant activatedAt,
                                  Instant finishedAt,
                                  BigDecimal authorizedAmount,
                                  BigDecimal capturedAmount,
                                  BigDecimal penaltyAmount,
                                  String paymentAuthorizationId) {
        return new Booking(id,
                customerId,
                providerId,
                vehicleId,
                startAt,
                endAt,
                state,
                createdAt,
                updatedAt,
                activatedAt,
                finishedAt,
                authorizedAmount,
                capturedAmount,
                penaltyAmount,
                paymentAuthorizationId);
    }

    public void cancel(Instant cancelledAt) {
        if (state == BookingState.CANCELLED) {
            return;
        }
        if (state == BookingState.FINISHED || state == BookingState.ACTIVE) {
            throw new BookingDomainException("Active or finished bookings cannot be cancelled");
        }
        if (!cancelledAt.isBefore(startAt)) {
            throw new BookingDomainException("Bookings can only be cancelled before the rental starts");
        }
        this.state = BookingState.CANCELLED;
        this.updatedAt = cancelledAt;
        domainEvents.add(new BookingCancelledEvent(id, cancelledAt, customerId, vehicleId, cancelledAt));
    }

    public void start(Long providerId, Instant startedAt) {
        if (!Objects.equals(this.providerId, providerId)) {
            throw new BookingDomainException("Provider is not allowed to start this rental");
        }
        if (state != BookingState.CONFIRMED) {
            throw new BookingDomainException("Only confirmed bookings can be activated");
        }
        if (startedAt.isBefore(startAt.minus(ALLOWED_EARLY_START_WINDOW))) {
            throw new BookingDomainException("Rental cannot be started before the allowed time window");
        }
        if (startedAt.isAfter(endAt)) {
            throw new BookingDomainException("Rental cannot be started after it is scheduled to finish");
        }
        this.state = BookingState.ACTIVE;
        this.activatedAt = startedAt;
        this.updatedAt = startedAt;
        domainEvents.add(new RentalStartedEvent(id, startedAt, providerId, vehicleId, startedAt));
    }

    public void finish(Long providerId, Instant finishedAt) {
        if (!Objects.equals(this.providerId, providerId)) {
            throw new BookingDomainException("Provider is not allowed to finish this rental");
        }
        if (state != BookingState.ACTIVE) {
            throw new BookingDomainException("Only active rentals can be finished");
        }
        if (activatedAt == null) {
            throw new BookingDomainException("Rental has not been activated");
        }
        if (finishedAt.isBefore(activatedAt)) {
            throw new BookingDomainException("Rental cannot be finished before it started");
        }
        this.state = BookingState.FINISHED;
        this.finishedAt = finishedAt;
        this.updatedAt = finishedAt;
        this.penaltyAmount = calculatePenalty(finishedAt);
        this.capturedAmount = authorizedAmount.add(penaltyAmount);
        domainEvents.add(new RentalFinishedEvent(id, finishedAt, providerId, vehicleId, finishedAt, capturedAmount, penaltyAmount));
    }

    private BigDecimal calculatePenalty(Instant finishedAt) {
        if (!finishedAt.isAfter(endAt)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        long minutesLate = Duration.between(endAt, finishedAt).toMinutes();
        long hoursLate = (minutesLate + 59) / 60;
        BigDecimal penalty = PENALTY_PER_HOUR.multiply(BigDecimal.valueOf(hoursLate));
        return penalty.setScale(2, RoundingMode.HALF_UP);
    }

    public List<BookingDomainEvent> pullDomainEvents() {
        List<BookingDomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    public UUID getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public BookingState getState() {
        return state;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getActivatedAt() {
        return activatedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public BigDecimal getAuthorizedAmount() {
        return authorizedAmount;
    }

    public BigDecimal getCapturedAmount() {
        return capturedAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public String getPaymentAuthorizationId() {
        return paymentAuthorizationId;
    }

    public BigDecimal getAmountToCapture() {
        return capturedAmount != null ? capturedAmount : authorizedAmount.add(penaltyAmount);
    }

    public boolean isOwnedByCustomer(Long customerId) {
        return Objects.equals(this.customerId, customerId);
    }

    public boolean isOwnedByProvider(Long providerId) {
        return Objects.equals(this.providerId, providerId);
    }
}
