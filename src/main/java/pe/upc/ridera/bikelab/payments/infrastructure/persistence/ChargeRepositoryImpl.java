package pe.upc.ridera.bikelab.payments.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Charge;
import pe.upc.ridera.bikelab.payments.domain.persistence.ChargeRepository;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities.ChargeEntity;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.repositories.ChargeJpaRepository;

@Repository
public class ChargeRepositoryImpl implements ChargeRepository {

    private final ChargeJpaRepository chargeJpaRepository;

    public ChargeRepositoryImpl(ChargeJpaRepository chargeJpaRepository) {
        this.chargeJpaRepository = chargeJpaRepository;
    }

    @Override
    public Charge save(Charge charge) {
        ChargeEntity entity = ChargeEntity.fromAggregate(charge);
        return chargeJpaRepository.save(entity).toAggregate();
    }

    @Override
    public Optional<Charge> findById(UUID chargeId) {
        return chargeJpaRepository.findById(chargeId).map(ChargeEntity::toAggregate);
    }

    @Override
    public Optional<Charge> findByIdempotencyKey(String idempotencyKey) {
        return chargeJpaRepository.findByIdempotencyKey(idempotencyKey).map(ChargeEntity::toAggregate);
    }
}
