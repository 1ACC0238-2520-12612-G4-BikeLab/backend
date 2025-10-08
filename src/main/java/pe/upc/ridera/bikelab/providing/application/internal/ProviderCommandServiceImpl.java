package pe.upc.ridera.bikelab.providing.application.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.providing.application.commands.ApproveProviderCommand;
import pe.upc.ridera.bikelab.providing.application.commands.RejectProviderCommand;
import pe.upc.ridera.bikelab.providing.application.commands.RequestOnboardingCommand;
import pe.upc.ridera.bikelab.providing.application.commands.SubmitKycCommand;
import pe.upc.ridera.bikelab.providing.application.dto.ProviderResource;
import pe.upc.ridera.bikelab.providing.application.events.ProviderEventPublisher;
import pe.upc.ridera.bikelab.providing.application.services.ProviderCommandService;
import pe.upc.ridera.bikelab.providing.domain.exceptions.ProviderNotFoundException;
import pe.upc.ridera.bikelab.providing.domain.model.aggregates.Provider;
import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;
import pe.upc.ridera.bikelab.providing.domain.persistence.ProviderRepository;

/**
 * Implementación de los casos de uso de escritura del bounded context Providing.
 */
@Service
@Transactional
public class ProviderCommandServiceImpl implements ProviderCommandService {

    private final ProviderRepository providerRepository;
    private final ProviderEventPublisher eventPublisher;
    private final ProviderMapper mapper;

    public ProviderCommandServiceImpl(ProviderRepository providerRepository, ProviderEventPublisher eventPublisher,
            ProviderMapper mapper) {
        this.providerRepository = providerRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    public ProviderResource requestOnboarding(RequestOnboardingCommand command) {
        return providerRepository.findByUserId(command.userId())
                .map(mapper::toResource)
                .orElseGet(() -> {
                    Provider provider = Provider.requestOnboarding(command.userId(), command.displayName(),
                            command.phone(), command.docType(), command.docNumber());
                    Provider saved = providerRepository.save(provider);
                    return mapper.toResource(saved);
                });
    }

    @Override
    public ProviderResource submitKyc(SubmitKycCommand command) {
        Provider provider = providerRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ProviderNotFoundException(command.userId()));
        provider.submitKyc(command.displayName(), command.phone(), command.docType(), command.docNumber());
        Provider saved = providerRepository.save(provider);
        return mapper.toResource(saved);
    }

    @Override
    public ProviderResource approveProvider(ApproveProviderCommand command) {
        Provider provider = providerRepository.findById(command.providerId())
                .orElseThrow(() -> new ProviderNotFoundException(command.providerId()));
        provider.approve(command.adminId(), command.reason());
        Provider saved = providerRepository.save(provider);
        if (saved.getStatus() == ProviderStatus.APPROVED) {
            eventPublisher.publish(saved.toApprovedEvent());
        }
        return mapper.toResource(saved);
    }

    @Override
    public ProviderResource rejectProvider(RejectProviderCommand command) {
        Provider provider = providerRepository.findById(command.providerId())
                .orElseThrow(() -> new ProviderNotFoundException(command.providerId()));
        provider.reject(command.adminId(), command.reason());
        Provider saved = providerRepository.save(provider);
        return mapper.toResource(saved);
    }
}
