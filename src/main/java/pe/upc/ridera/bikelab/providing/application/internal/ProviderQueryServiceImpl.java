package pe.upc.ridera.bikelab.providing.application.internal;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.providing.application.dto.ProviderResource;
import pe.upc.ridera.bikelab.providing.application.queries.GetProviderByUserIdQuery;
import pe.upc.ridera.bikelab.providing.application.queries.ListProvidersQuery;
import pe.upc.ridera.bikelab.providing.application.services.ProviderQueryService;
import pe.upc.ridera.bikelab.providing.domain.persistence.ProviderRepository;
import pe.upc.ridera.bikelab.providing.domain.persistence.criteria.ProviderSearchCriteria;

/**
 * Implementación de los casos de uso de consulta del bounded context Providing.
 */
@Service
@Transactional(readOnly = true)
public class ProviderQueryServiceImpl implements ProviderQueryService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper mapper;

    public ProviderQueryServiceImpl(ProviderRepository providerRepository, ProviderMapper mapper) {
        this.providerRepository = providerRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProviderResource> getProvider(GetProviderByUserIdQuery query) {
        return providerRepository.findByUserId(query.userId())
                .map(mapper::toResource);
    }

    @Override
    public List<ProviderResource> listProviders(ListProvidersQuery query) {
        ProviderSearchCriteria criteria = ProviderSearchCriteria.empty();
        if (query.status().isPresent()) {
            criteria = criteria.withStatus(query.status().get());
        }
        if (query.search().isPresent() && !query.search().get().isBlank()) {
            criteria = criteria.withSearch(query.search().get());
        }
        return providerRepository.findAll(criteria).stream()
                .map(mapper::toResource)
                .toList();
    }
}
