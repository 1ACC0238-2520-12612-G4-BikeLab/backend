package pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;
import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities.ChargeEntity;

public interface ChargeJpaRepository extends JpaRepository<ChargeEntity, UUID> {

    Optional<ChargeEntity> findByIdempotencyKey(String idempotencyKey);

    long countByStatus(ChargeStatus status);
}
