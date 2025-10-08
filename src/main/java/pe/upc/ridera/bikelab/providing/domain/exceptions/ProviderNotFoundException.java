package pe.upc.ridera.bikelab.providing.domain.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando no se encuentra un proveedor.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProviderNotFoundException extends ProvidingDomainException {

    public ProviderNotFoundException(UUID providerId) {
        super("Proveedor " + providerId + " no encontrado");
    }

    public ProviderNotFoundException(UUID providerId, String context) {
        super(context + ": proveedor " + providerId + " no encontrado");
    }
}
