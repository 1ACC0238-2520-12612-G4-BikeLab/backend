package pe.upc.ridera.bikelab.providing.application.services;

import java.util.UUID;

/**
 * Servicio utilizado por otros bounded contexts para validar el estado de un proveedor.
 */
public interface ProviderApprovalService {

    boolean isProviderApproved(UUID userId);

    void ensureProviderApproved(UUID userId);
}
