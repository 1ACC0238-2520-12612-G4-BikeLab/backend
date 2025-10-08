package pe.upc.ridera.bikelab.payments.domain.exceptions;

/**
 * Excepción base del dominio de pagos utilizada para representar errores de negocio.
 */
public class PaymentDomainException extends RuntimeException {

    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
