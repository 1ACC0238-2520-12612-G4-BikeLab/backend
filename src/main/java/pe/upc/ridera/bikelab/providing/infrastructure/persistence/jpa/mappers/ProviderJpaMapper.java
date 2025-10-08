package pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.mappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.providing.domain.model.aggregates.Provider;
import pe.upc.ridera.bikelab.providing.domain.model.entities.ProviderVerification;
import pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.entities.ProviderEntity;
import pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.entities.ProviderVerificationEntity;

/**
 * Mapper encargado de convertir entre entidades JPA y agregados de dominio.
 */
@Component
public class ProviderJpaMapper {

    public ProviderEntity toEntity(Provider provider) {
        ProviderEntity entity = new ProviderEntity(provider.getId(), provider.getUserId(), provider.getStatus(),
                provider.getDisplayName(), provider.getPhone(), provider.getDocType(), provider.getDocNumber(),
                provider.getCreatedAt(), provider.getUpdatedAt());
        List<ProviderVerificationEntity> verificationEntities = new ArrayList<>();
        provider.getVerifications().forEach(verification -> {
            ProviderVerificationEntity verificationEntity = new ProviderVerificationEntity(verification.getId(),
                    entity, verification.getOldStatus(), verification.getNewStatus(), verification.getActorId(),
                    verification.getReason(), verification.getCreatedAt());
            verificationEntity.setProvider(entity);
            verificationEntities.add(verificationEntity);
        });
        entity.setVerifications(verificationEntities);
        return entity;
    }

    public Provider toAggregate(ProviderEntity entity) {
        List<ProviderVerification> verifications = entity.getVerifications().stream()
                .map(v -> ProviderVerification.rehydrate(v.getId(), entity.getId(), v.getOldStatus(), v.getNewStatus(),
                        v.getActorId(), v.getReason(), v.getCreatedAt()))
                .sorted(Comparator.comparing(ProviderVerification::getCreatedAt))
                .toList();
        return Provider.rehydrate(entity.getId(), entity.getUserId(), entity.getStatus(), entity.getDisplayName(),
                entity.getPhone(), entity.getDocType(), entity.getDocNumber(), entity.getCreatedAt(),
                entity.getUpdatedAt(), verifications);
    }
}
