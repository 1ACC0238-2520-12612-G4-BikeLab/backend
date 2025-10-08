package pe.upc.ridera.bikelab.providing.domain.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando un usuario intenta operar como proveedor sin estar aprobado.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ProviderApprovalRequiredException extends ProvidingDomainException {

    public ProviderApprovalRequiredException(UUID userId) {
        super("El usuario " + userId + " debe tener un proveedor aprobado para realizar esta operación");
    }
}
