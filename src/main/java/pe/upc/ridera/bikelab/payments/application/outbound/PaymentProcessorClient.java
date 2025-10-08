package pe.upc.ridera.bikelab.payments.application.outbound;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Puerto de salida encargado de invocar al PSP externo.
 */
public interface PaymentProcessorClient {

    AuthorizationResponse authorize(Long customerId,
                                    UUID bookingId,
                                    BigDecimal amount,
                                    String currency,
                                    String paymentToken,
                                    String idempotencyKey);

    void capture(String processorReference, BigDecimal amount, String idempotencyKey);

    void refund(String processorReference, BigDecimal amount, String reason);

    record AuthorizationResponse(String processorReference) {
    }
}
