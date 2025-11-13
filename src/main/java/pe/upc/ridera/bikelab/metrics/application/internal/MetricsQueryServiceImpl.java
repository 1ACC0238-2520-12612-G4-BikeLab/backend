package pe.upc.ridera.bikelab.metrics.application.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.iam.domain.persistence.UserRepository;
import pe.upc.ridera.bikelab.metrics.application.dto.MetricsResource;
import pe.upc.ridera.bikelab.metrics.application.services.MetricsQueryService;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;
import pe.upc.ridera.bikelab.payments.domain.persistence.ChargeRepository;
import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;
import pe.upc.ridera.bikelab.providing.domain.persistence.ProviderRepository;
import pe.upc.ridera.bikelab.renting.domain.model.valueobjects.BookingState;
import pe.upc.ridera.bikelab.renting.domain.persistence.BookingRepository;
import pe.upc.ridera.bikelab.vehicles.domain.model.valueobjects.VehicleStatus;
import pe.upc.ridera.bikelab.vehicles.domain.persistence.VehicleRepository;

/**
 * Implementación que compone datos de varios bounded contexts para exponer un resumen global.
 */
@Service
@Transactional(readOnly = true)
public class MetricsQueryServiceImpl implements MetricsQueryService {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final ChargeRepository chargeRepository;

    public MetricsQueryServiceImpl(UserRepository userRepository,
                                   ProviderRepository providerRepository,
                                   VehicleRepository vehicleRepository,
                                   BookingRepository bookingRepository,
                                   ChargeRepository chargeRepository) {
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
        this.vehicleRepository = vehicleRepository;
        this.bookingRepository = bookingRepository;
        this.chargeRepository = chargeRepository;
    }

    @Override
    public MetricsResource getOverview() {
        long usersTotal = userRepository.count();
        long providersApproved = providerRepository.countByStatus(ProviderStatus.APPROVED);
        long vehiclesAvailable = vehicleRepository.countByStatus(VehicleStatus.AVAILABLE);
        long vehiclesInService = vehicleRepository.countByStatus(VehicleStatus.IN_SERVICE);
        long bookingsConfirmed = bookingRepository.countByState(BookingState.CONFIRMED);
        long bookingsActive = bookingRepository.countByState(BookingState.ACTIVE);
        long bookingsFinished = bookingRepository.countByState(BookingState.FINISHED);
        long paymentsAuthorized = chargeRepository.countByStatus(ChargeStatus.AUTHORIZED);
        long paymentsCaptured = chargeRepository.countByStatus(ChargeStatus.CAPTURED);

        return new MetricsResource(
                usersTotal,
                providersApproved,
                vehiclesAvailable,
                vehiclesInService,
                bookingsConfirmed,
                bookingsActive,
                bookingsFinished,
                paymentsAuthorized,
                paymentsCaptured);
    }
}
