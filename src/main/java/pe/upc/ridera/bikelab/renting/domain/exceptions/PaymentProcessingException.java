package pe.upc.ridera.bikelab.renting.domain.exceptions;

/**
 * Esta clase modela las fallas relacionadas con el procesamiento de pagos asociados a una reserva.
 */
public class PaymentProcessingException extends RuntimeException {

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentProcessingException(String message) {
        super(message);
    }
}
