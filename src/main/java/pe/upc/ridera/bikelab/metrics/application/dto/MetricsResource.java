package pe.upc.ridera.bikelab.metrics.application.dto;

/**
 * Recurso que resume los contadores globales expuestos por el bounded context Metrics.
 */
public record MetricsResource(
        long usersTotal,
        long providersApproved,
        long vehiclesAvailable,
        long vehiclesInService,
        long bookingsConfirmed,
        long bookingsActive,
        long bookingsFinished,
        long paymentsAuthorized,
        long paymentsCaptured) {
}
