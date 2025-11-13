package pe.upc.ridera.bikelab.payments.domain.persistence;

import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Charge;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;

/**
 * Contrato del repositorio encargado de persistir los cargos.
 */
public interface ChargeRepository {

    Charge save(Charge charge);

    Optional<Charge> findById(UUID chargeId);

    Optional<Charge> findByIdempotencyKey(String idempotencyKey);

    long countByStatus(ChargeStatus status);
}
