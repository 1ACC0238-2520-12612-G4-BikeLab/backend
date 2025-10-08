package pe.upc.ridera.bikelab.payments.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.application.dto.ChargeResource;
import pe.upc.ridera.bikelab.payments.application.dto.PaymentMethodResource;
import pe.upc.ridera.bikelab.payments.application.dto.PayoutResource;

/**
 * Casos de uso expuestos por el bounded context de pagos.
 */
public interface PaymentService {

    ChargeResource authorizeCharge(UUID bookingId,
                                   Long customerId,
                                   UUID paymentMethodId,
                                   BigDecimal amount,
                                   String currency,
                                   String idempotencyKey);

    ChargeResource captureCharge(UUID chargeId, Long actorId, boolean overrideOwnership, String idempotencyKey);

    ChargeResource refundCharge(UUID chargeId, BigDecimal amount, String reason, boolean force);

    PaymentMethodResource addPaymentMethod(Long customerId,
                                           String tokenRef,
                                           String brand,
                                           String last4,
                                           boolean makeDefault);

    void removePaymentMethod(Long customerId, UUID methodId);

    List<PaymentMethodResource> listPaymentMethods(Long customerId);

    List<PayoutResource> listPayouts(Long providerId);

    void releaseAuthorization(UUID chargeId);
}
