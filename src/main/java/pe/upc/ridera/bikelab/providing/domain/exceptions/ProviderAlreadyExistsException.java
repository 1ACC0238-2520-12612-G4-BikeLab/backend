package pe.upc.ridera.bikelab.providing.domain.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta crear un proveedor para un usuario que ya tiene uno.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ProviderAlreadyExistsException extends ProvidingDomainException {

    public ProviderAlreadyExistsException(UUID userId) {
        super("El usuario " + userId + " ya cuenta con un proveedor registrado");
    }
}
