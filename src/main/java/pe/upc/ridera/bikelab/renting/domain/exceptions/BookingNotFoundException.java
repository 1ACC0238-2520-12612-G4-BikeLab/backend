package pe.upc.ridera.bikelab.renting.domain.exceptions;

import java.util.UUID;

/**
 * Esta clase representa la excepción lanzada cuando no se encuentra una reserva solicitada.
 */
public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(UUID bookingId) {
        super("Booking " + bookingId + " was not found");
    }
}
