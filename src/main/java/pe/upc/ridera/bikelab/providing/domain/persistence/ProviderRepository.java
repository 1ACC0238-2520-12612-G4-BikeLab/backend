package pe.upc.ridera.bikelab.providing.domain.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.upc.ridera.bikelab.providing.domain.model.aggregates.Provider;
import pe.upc.ridera.bikelab.providing.domain.persistence.criteria.ProviderSearchCriteria;

/**
 * Contrato para la persistencia del agregado Provider.
 */
public interface ProviderRepository {

    Provider save(Provider provider);

    Optional<Provider> findById(UUID providerId);

    Optional<Provider> findByUserId(UUID userId);

    List<Provider> findAll(ProviderSearchCriteria criteria);
}
