package pe.upc.ridera.bikelab.providing.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;

/**
 * Indica que una transición de estado es inválida.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProviderStatusException extends ProvidingDomainException {

    public ProviderStatusException(ProviderStatus currentStatus, String operation) {
        super("No se puede " + operation + " cuando el proveedor está en estado " + currentStatus);
    }
}
