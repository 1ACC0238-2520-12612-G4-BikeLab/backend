package pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities.PaymentMethodEntity;

public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodEntity, UUID> {

    List<PaymentMethodEntity> findByCustomerId(Long customerId);
}
