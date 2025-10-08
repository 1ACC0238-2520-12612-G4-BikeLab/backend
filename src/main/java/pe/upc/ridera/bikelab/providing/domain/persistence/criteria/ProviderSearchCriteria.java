package pe.upc.ridera.bikelab.providing.domain.persistence.criteria;

import java.util.Optional;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Criterios básicos para listar proveedores.
 */
public record ProviderSearchCriteria(Optional<ProviderStatus> status, Optional<String> search) {

    public static ProviderSearchCriteria empty() {
        return new ProviderSearchCriteria(Optional.empty(), Optional.empty());
    }

    public ProviderSearchCriteria withStatus(ProviderStatus status) {
        return new ProviderSearchCriteria(Optional.ofNullable(status), search);
    }

    public ProviderSearchCriteria withSearch(String value) {
        return new ProviderSearchCriteria(status, Optional.ofNullable(value));
    }
}
