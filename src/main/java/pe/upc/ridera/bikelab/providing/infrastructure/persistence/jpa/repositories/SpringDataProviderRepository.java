package pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.entities.ProviderEntity;

public interface SpringDataProviderRepository
        extends JpaRepository<ProviderEntity, UUID>, JpaSpecificationExecutor<ProviderEntity> {

    Optional<ProviderEntity> findByUserId(UUID userId);
}
