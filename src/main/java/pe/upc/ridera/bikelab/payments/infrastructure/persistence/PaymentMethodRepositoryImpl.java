package pe.upc.ridera.bikelab.payments.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.PaymentMethod;
import pe.upc.ridera.bikelab.payments.domain.persistence.PaymentMethodRepository;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities.PaymentMethodEntity;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.repositories.PaymentMethodJpaRepository;

@Repository
public class PaymentMethodRepositoryImpl implements PaymentMethodRepository {

    private final PaymentMethodJpaRepository paymentMethodJpaRepository;

    public PaymentMethodRepositoryImpl(PaymentMethodJpaRepository paymentMethodJpaRepository) {
        this.paymentMethodJpaRepository = paymentMethodJpaRepository;
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        PaymentMethodEntity entity = PaymentMethodEntity.fromAggregate(paymentMethod);
        return paymentMethodJpaRepository.save(entity).toAggregate();
    }

    @Override
    public void delete(PaymentMethod paymentMethod) {
        paymentMethodJpaRepository.deleteById(paymentMethod.getId());
    }

    @Override
    public Optional<PaymentMethod> findById(UUID methodId) {
        return paymentMethodJpaRepository.findById(methodId).map(PaymentMethodEntity::toAggregate);
    }

    @Override
    public List<PaymentMethod> findByCustomerId(Long customerId) {
        return paymentMethodJpaRepository.findByCustomerId(customerId).stream()
                .map(PaymentMethodEntity::toAggregate)
                .toList();
    }
}
