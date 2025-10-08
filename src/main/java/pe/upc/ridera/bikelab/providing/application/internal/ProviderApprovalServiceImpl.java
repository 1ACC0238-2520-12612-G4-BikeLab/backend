package pe.upc.ridera.bikelab.providing.application.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.providing.application.services.ProviderApprovalService;
import pe.upc.ridera.bikelab.providing.domain.exceptions.ProviderApprovalRequiredException;
import pe.upc.ridera.bikelab.providing.domain.model.aggregates.Provider;
import pe.upc.ridera.bikelab.providing.domain.persistence.ProviderRepository;

/**
 * Servicio que permite verificar el estado de aprobación del proveedor autenticado.
 */
@Service
@Transactional(readOnly = true)
public class ProviderApprovalServiceImpl implements ProviderApprovalService {

    private final ProviderRepository providerRepository;

    public ProviderApprovalServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public boolean isProviderApproved(UUID userId) {
        return providerRepository.findByUserId(userId)
                .map(Provider::isApproved)
                .orElse(false);
    }

    @Override
    public void ensureProviderApproved(UUID userId) {
        if (!isProviderApproved(userId)) {
            throw new ProviderApprovalRequiredException(userId);
        }
    }
}
