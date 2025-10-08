package pe.upc.ridera.bikelab.providing.application.queries;

import java.util.Optional;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Consulta para listar proveedores filtrando por estado o término de búsqueda.
 */
public record ListProvidersQuery(Optional<ProviderStatus> status, Optional<String> search) {

    public static ListProvidersQuery empty() {
        return new ListProvidersQuery(Optional.empty(), Optional.empty());
    }
}
