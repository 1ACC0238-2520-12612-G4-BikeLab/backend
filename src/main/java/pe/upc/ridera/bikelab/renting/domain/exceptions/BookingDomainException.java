package pe.upc.ridera.bikelab.renting.domain.exceptions;

/**
 * Esta clase representa la excepción base para errores de dominio durante el ciclo de vida de una reserva.
 */
public class BookingDomainException extends RuntimeException {

    public BookingDomainException(String message) {
        super(message);
    }
}
