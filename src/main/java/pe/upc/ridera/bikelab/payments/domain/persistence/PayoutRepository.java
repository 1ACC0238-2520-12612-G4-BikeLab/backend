package pe.upc.ridera.bikelab.payments.domain.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Payout;

/**
 * Contrato del repositorio encargado de los pagos hacia proveedores.
 */
public interface PayoutRepository {

    Payout save(Payout payout);

    Optional<Payout> findById(UUID payoutId);

    List<Payout> findByProviderId(Long providerId);
}
