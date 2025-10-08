package pe.upc.ridera.bikelab.iam.application.services;

import pe.upc.ridera.bikelab.iam.application.commands.AuthenticateUserCommand;
import pe.upc.ridera.bikelab.iam.application.commands.OnboardProviderCommand;
import pe.upc.ridera.bikelab.iam.application.commands.RegisterUserCommand;
import pe.upc.ridera.bikelab.iam.application.dto.AuthResponse;
import pe.upc.ridera.bikelab.iam.application.dto.UserResource;

/**
 * Este servicio orquesta los comandos que modifican el estado de los usuarios.
 */ 
public interface UserCommandService {

    UserResource handle(RegisterUserCommand command);

    AuthResponse handle(AuthenticateUserCommand command);

    UserResource handle(OnboardProviderCommand command);
}
