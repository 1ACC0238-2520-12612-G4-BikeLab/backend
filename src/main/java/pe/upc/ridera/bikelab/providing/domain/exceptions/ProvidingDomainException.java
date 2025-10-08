package pe.upc.ridera.bikelab.providing.domain.exceptions;

/**
 * Excepción base para el dominio de proveedores.
 */
public class ProvidingDomainException extends RuntimeException {

    public ProvidingDomainException(String message) {
        super(message);
    }
}
