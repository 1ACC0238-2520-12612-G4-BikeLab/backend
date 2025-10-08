package pe.upc.ridera.bikelab.payments.domain.model.valueobjects;

/**
 * Estados válidos de un cargo procesado por la pasarela de pagos.
 */
public enum ChargeStatus {
    AUTHORIZED,
    CAPTURED,
    REFUNDED,
    FAILED
}
