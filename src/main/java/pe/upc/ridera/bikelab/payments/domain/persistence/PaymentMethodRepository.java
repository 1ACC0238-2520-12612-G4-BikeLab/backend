package pe.upc.ridera.bikelab.payments.domain.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.PaymentMethod;

/**
 * Contrato del repositorio de métodos de pago.
 */
public interface PaymentMethodRepository {

    PaymentMethod save(PaymentMethod paymentMethod);

    void delete(PaymentMethod paymentMethod);

    Optional<PaymentMethod> findById(UUID methodId);

    List<PaymentMethod> findByCustomerId(Long customerId);
}
