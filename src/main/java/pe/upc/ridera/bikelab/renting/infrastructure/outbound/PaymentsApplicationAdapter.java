package pe.upc.ridera.bikelab.renting.infrastructure.outbound;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.payments.application.dto.ChargeResource;
import pe.upc.ridera.bikelab.payments.application.services.PaymentService;
import pe.upc.ridera.bikelab.renting.application.outbound.PaymentsPort;

@Component
public class PaymentsApplicationAdapter implements PaymentsPort {

    private final PaymentService paymentService;

    public PaymentsApplicationAdapter(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public PaymentAuthorization authorize(Long customerId, UUID bookingId, BigDecimal amount, String currency,
                                          String idempotencyKey) {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(bookingId, "bookingId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        ChargeResource charge = paymentService.authorizeCharge(bookingId, customerId, null, amount, currency, idempotencyKey);
        return new PaymentAuthorization(charge.id().toString(), charge.amount());
    }

    @Override
    public void capture(String chargeId, Long customerId, boolean overrideOwnership, String idempotencyKey) {
        Objects.requireNonNull(chargeId, "chargeId");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        UUID id = UUID.fromString(chargeId);
        paymentService.captureCharge(id, customerId, overrideOwnership, idempotencyKey);
    }

    @Override
    public void release(String chargeId) {
        Objects.requireNonNull(chargeId, "chargeId");
        UUID id = UUID.fromString(chargeId);
        paymentService.releaseAuthorization(id);
    }
}
