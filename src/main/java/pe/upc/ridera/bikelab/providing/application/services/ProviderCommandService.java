package pe.upc.ridera.bikelab.providing.application.services;

import pe.upc.ridera.bikelab.providing.application.commands.ApproveProviderCommand;
import pe.upc.ridera.bikelab.providing.application.commands.RejectProviderCommand;
import pe.upc.ridera.bikelab.providing.application.commands.RequestOnboardingCommand;
import pe.upc.ridera.bikelab.providing.application.commands.SubmitKycCommand;
import pe.upc.ridera.bikelab.providing.application.dto.ProviderResource;

/**
 * Casos de uso de escritura para el agregado Provider.
 */
public interface ProviderCommandService {

    ProviderResource requestOnboarding(RequestOnboardingCommand command);

    ProviderResource submitKyc(SubmitKycCommand command);

    ProviderResource approveProvider(ApproveProviderCommand command);

    ProviderResource rejectProvider(RejectProviderCommand command);
}
