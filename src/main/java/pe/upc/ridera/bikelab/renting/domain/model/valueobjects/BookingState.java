package pe.upc.ridera.bikelab.renting.domain.model.valueobjects;

/**
 * Esta enumeración define los posibles estados del ciclo de vida de una reserva.
 */
public enum BookingState {
    PENDING,
    CONFIRMED,
    ACTIVE,
    FINISHED,
    CANCELLED
}
