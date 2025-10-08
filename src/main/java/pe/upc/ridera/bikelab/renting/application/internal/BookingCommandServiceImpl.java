package pe.upc.ridera.bikelab.renting.application.internal;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.renting.application.commands.CancelBookingCommand;
import pe.upc.ridera.bikelab.renting.application.commands.CreateBookingCommand;
import pe.upc.ridera.bikelab.renting.application.commands.FinishRentalCommand;
import pe.upc.ridera.bikelab.renting.application.commands.StartRentalCommand;
import pe.upc.ridera.bikelab.renting.application.events.DomainEventPublisher;
import pe.upc.ridera.bikelab.renting.application.outbound.PaymentsPort;
import pe.upc.ridera.bikelab.renting.application.outbound.VehicleCatalogPort;
import pe.upc.ridera.bikelab.renting.application.outbound.PaymentsPort.PaymentAuthorization;
import pe.upc.ridera.bikelab.renting.application.outbound.VehicleCatalogPort.VehicleSnapshot;
import pe.upc.ridera.bikelab.renting.application.services.BookingCommandService;
import pe.upc.ridera.bikelab.renting.domain.exceptions.BookingDomainException;
import pe.upc.ridera.bikelab.renting.domain.exceptions.BookingNotFoundException;
import pe.upc.ridera.bikelab.renting.domain.exceptions.PaymentProcessingException;
import pe.upc.ridera.bikelab.renting.domain.exceptions.VehicleUnavailableException;
import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;
import pe.upc.ridera.bikelab.renting.domain.model.events.BookingDomainEvent;
import pe.upc.ridera.bikelab.renting.domain.persistence.BookingRepository;

@Service
@Transactional
/**
 * Esta clase implementa los casos de uso que modifican el ciclo de vida de las reservas y coordinan integraciones externas.
 */
public class BookingCommandServiceImpl implements BookingCommandService {

    private static final int PAYMENT_RETRY_ATTEMPTS = 3;
    private static final String DEFAULT_CURRENCY = "PEN";

    private final BookingRepository bookingRepository;
    private final VehicleCatalogPort vehicleCatalogPort;
    private final PaymentsPort paymentsPort;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public BookingCommandServiceImpl(BookingRepository bookingRepository,
                                     VehicleCatalogPort vehicleCatalogPort,
                                     PaymentsPort paymentsPort,
                                     DomainEventPublisher domainEventPublisher,
                                     Clock clock) {
        this.bookingRepository = Objects.requireNonNull(bookingRepository);
        this.vehicleCatalogPort = Objects.requireNonNull(vehicleCatalogPort);
        this.paymentsPort = Objects.requireNonNull(paymentsPort);
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public Booking handle(CreateBookingCommand command) {
        Instant now = Instant.now(clock);
        if (command.startAt() == null || command.endAt() == null || command.vehicleId() == null || command.customerId() == null) {
            throw new BookingDomainException("Booking creation parameters are required");
        }
        if (!command.startAt().isBefore(command.endAt())) {
            throw new BookingDomainException("The start date must be before the end date");
        }
        if (!command.startAt().isAfter(now)) {
            throw new BookingDomainException("Bookings must start in the future");
        }

        VehicleSnapshot snapshot = vehicleCatalogPort.getVehicleSnapshot(command.vehicleId());
        if (snapshot == null) {
            throw new VehicleUnavailableException(command.vehicleId());
        }
        if (!vehicleCatalogPort.isAvailable(command.vehicleId(), command.startAt(), command.endAt())) {
            throw new VehicleUnavailableException(command.vehicleId());
        }
        if (bookingRepository.existsActiveBookingForVehicle(command.vehicleId(), command.startAt(), command.endAt())) {
            throw new VehicleUnavailableException(command.vehicleId());
        }

        BigDecimal amount = vehicleCatalogPort.quote(command.vehicleId(), command.startAt(), command.endAt());
        UUID bookingId = UUID.randomUUID();
        String idempotencyKey = "booking-authorize-" + bookingId;
        PaymentAuthorization authorization = retry(
                () -> paymentsPort.authorize(command.customerId(), bookingId, amount, DEFAULT_CURRENCY, idempotencyKey),
                "authorize payment");

        Booking booking = Booking.createWithId(bookingId, command.customerId(), snapshot.providerId(), command.vehicleId(),
                command.startAt(), command.endAt(), authorization.amount(), authorization.authorizationId(), now);
        bookingRepository.save(booking);
        vehicleCatalogPort.reserve(command.vehicleId(), command.startAt(), command.endAt());
        publishEvents(booking);
        return booking;
    }

    @Override
    public Booking handle(CancelBookingCommand command) {
        Booking booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new BookingNotFoundException(command.bookingId()));
        if (!booking.isOwnedByCustomer(command.customerId())) {
            throw new AccessDeniedException("Customer is not owner of booking");
        }
        Instant now = Instant.now(clock);
        booking.cancel(now);
        bookingRepository.save(booking);
        vehicleCatalogPort.release(booking.getVehicleId());
        if (booking.getPaymentAuthorizationId() != null) {
            retryVoid(() -> paymentsPort.release(booking.getPaymentAuthorizationId()), "release payment");
        }
        publishEvents(booking);
        return booking;
    }

    @Override
    public Booking handle(StartRentalCommand command) {
        Booking booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new BookingNotFoundException(command.bookingId()));
        if (!booking.isOwnedByProvider(command.providerId())) {
            throw new AccessDeniedException("Provider is not owner of vehicle");
        }
        Instant now = Instant.now(clock);
        booking.start(command.providerId(), now);
        bookingRepository.save(booking);
        publishEvents(booking);
        return booking;
    }

    @Override
    public Booking handle(FinishRentalCommand command) {
        Booking booking = bookingRepository.findById(command.bookingId())
                .orElseThrow(() -> new BookingNotFoundException(command.bookingId()));
        if (!booking.isOwnedByProvider(command.providerId())) {
            throw new AccessDeniedException("Provider is not owner of vehicle");
        }
        Instant now = Instant.now(clock);
        booking.finish(command.providerId(), now);
        bookingRepository.save(booking);
        vehicleCatalogPort.release(booking.getVehicleId());
        String captureIdempotencyKey = "booking-capture-" + booking.getId();
        retryVoid(() -> paymentsPort.capture(booking.getPaymentAuthorizationId(), booking.getCustomerId(), false,
                        captureIdempotencyKey),
                "capture payment");
        publishEvents(booking);
        return booking;
    }

    private void publishEvents(Booking booking) {
        List<BookingDomainEvent> events = booking.pullDomainEvents();
        domainEventPublisher.publishAll(events);
    }

    private PaymentAuthorization retry(Supplier<PaymentAuthorization> supplier, String operation) {
        PaymentProcessingException exception = null;
        for (int attempt = 0; attempt < PAYMENT_RETRY_ATTEMPTS; attempt++) {
            try {
                return supplier.get();
            } catch (RuntimeException ex) {
                exception = new PaymentProcessingException("Failed to " + operation + " (attempt " + (attempt + 1) + ")", ex);
            }
        }
        throw exception;
    }

    private void retryVoid(Runnable action, String operation) {
        PaymentProcessingException exception = null;
        for (int attempt = 0; attempt < PAYMENT_RETRY_ATTEMPTS; attempt++) {
            try {
                action.run();
                return;
            } catch (RuntimeException ex) {
                exception = new PaymentProcessingException("Failed to " + operation + " (attempt " + (attempt + 1) + ")", ex);
            }
        }
        throw exception;
    }
}
