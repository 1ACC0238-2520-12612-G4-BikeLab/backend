package pe.upc.ridera.bikelab.payments.domain.model.aggregates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.exceptions.PaymentDomainException;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.PayoutStatus;

/**
 * Agregado que representa la conciliación y pago a un proveedor.
 */
public class Payout {

    private final UUID id;
    private final Long providerId;
    private final BigDecimal amount;
    private PayoutStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Payout(UUID id,
                   Long providerId,
                   BigDecimal amount,
                   PayoutStatus status,
                   Instant createdAt,
                   Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.providerId = Objects.requireNonNull(providerId, "providerId");
        this.amount = Objects.requireNonNull(amount, "amount").setScale(2, RoundingMode.HALF_UP);
        if (this.amount.signum() <= 0) {
            throw new PaymentDomainException("Payout amount must be greater than zero");
        }
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public static Payout schedule(Long providerId, BigDecimal amount, Instant createdAt) {
        return new Payout(UUID.randomUUID(), providerId, amount, PayoutStatus.PENDING, createdAt, createdAt);
    }

    public static Payout restore(UUID id,
                                 Long providerId,
                                 BigDecimal amount,
                                 PayoutStatus status,
                                 Instant createdAt,
                                 Instant updatedAt) {
        return new Payout(id, providerId, amount, status, createdAt, updatedAt);
    }

    public void markPaid(Instant paidAt) {
        this.status = PayoutStatus.PAID;
        this.updatedAt = paidAt;
    }

    public void markFailed(Instant failedAt) {
        this.status = PayoutStatus.FAILED;
        this.updatedAt = failedAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getProviderId() {
        return providerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PayoutStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
