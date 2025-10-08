package pe.upc.ridera.bikelab.providing.application.internal;

import java.util.List;

import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.providing.application.dto.ProviderResource;
import pe.upc.ridera.bikelab.providing.application.dto.ProviderVerificationResource;
import pe.upc.ridera.bikelab.providing.domain.model.aggregates.Provider;
import pe.upc.ridera.bikelab.providing.domain.model.entities.ProviderVerification;

/**
 * Mapper entre entidades de dominio y recursos REST.
 */
@Component
public class ProviderMapper {

    public ProviderResource toResource(Provider provider) {
        List<ProviderVerificationResource> verifications = provider.getVerifications().stream()
                .map(this::toResource)
                .toList();
        return new ProviderResource(provider.getId(), provider.getUserId(), provider.getStatus(),
                provider.getDisplayName(), provider.getPhone(), provider.getDocType(), provider.getDocNumber(),
                provider.getCreatedAt(), provider.getUpdatedAt(), verifications);
    }

    private ProviderVerificationResource toResource(ProviderVerification verification) {
        return new ProviderVerificationResource(verification.getId(), verification.getOldStatus(),
                verification.getNewStatus(), verification.getActorId(), verification.getReason(),
                verification.getCreatedAt());
    }
}
