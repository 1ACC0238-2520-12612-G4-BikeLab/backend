package pe.upc.ridera.bikelab.providing.application.services;

import java.util.List;
import java.util.Optional;

import pe.upc.ridera.bikelab.providing.application.dto.ProviderResource;
import pe.upc.ridera.bikelab.providing.application.queries.GetProviderByUserIdQuery;
import pe.upc.ridera.bikelab.providing.application.queries.ListProvidersQuery;

/**
 * Casos de uso de lectura para el agregado Provider.
 */
public interface ProviderQueryService {

    Optional<ProviderResource> getProvider(GetProviderByUserIdQuery query);

    List<ProviderResource> listProviders(ListProvidersQuery query);
}
