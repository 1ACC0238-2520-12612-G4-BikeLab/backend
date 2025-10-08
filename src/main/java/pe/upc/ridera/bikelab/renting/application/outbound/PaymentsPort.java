package pe.upc.ridera.bikelab.renting.application.outbound;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Esta interfaz declara las operaciones de pagos utilizadas para autorizar y capturar transacciones de reservas.
 */
public interface PaymentsPort {

    PaymentAuthorization authorize(Long customerId, UUID bookingId, BigDecimal amount, String currency,
                                   String idempotencyKey);

    void capture(String chargeId, Long customerId, boolean overrideOwnership, String idempotencyKey);

    void release(String chargeId);

    record PaymentAuthorization(String authorizationId, BigDecimal amount) {
    }
}
