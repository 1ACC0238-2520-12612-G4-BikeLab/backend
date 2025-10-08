package pe.upc.ridera.bikelab.payments.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Payout;
import pe.upc.ridera.bikelab.payments.domain.persistence.PayoutRepository;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities.PayoutEntity;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.repositories.PayoutJpaRepository;

@Repository
public class PayoutRepositoryImpl implements PayoutRepository {

    private final PayoutJpaRepository payoutJpaRepository;

    public PayoutRepositoryImpl(PayoutJpaRepository payoutJpaRepository) {
        this.payoutJpaRepository = payoutJpaRepository;
    }

    @Override
    public Payout save(Payout payout) {
        PayoutEntity entity = PayoutEntity.fromAggregate(payout);
        return payoutJpaRepository.save(entity).toAggregate();
    }

    @Override
    public Optional<Payout> findById(UUID payoutId) {
        return payoutJpaRepository.findById(payoutId).map(PayoutEntity::toAggregate);
    }

    @Override
    public List<Payout> findByProviderId(Long providerId) {
        return payoutJpaRepository.findByProviderId(providerId).stream()
                .map(PayoutEntity::toAggregate)
                .toList();
    }
}
