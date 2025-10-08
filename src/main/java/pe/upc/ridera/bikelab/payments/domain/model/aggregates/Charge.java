package pe.upc.ridera.bikelab.payments.domain.model.aggregates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.exceptions.PaymentDomainException;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentAuthorizedEvent;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentCapturedEvent;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentDomainEvent;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentRefundedEvent;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;

/**
 * Agregado que modela el ciclo de vida de un cargo realizado a un cliente.
 */
public class Charge {

    private final UUID id;
    private final UUID bookingId;
    private final Long customerId;
    private final BigDecimal amount;
    private final String currency;
    private ChargeStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private final String idempotencyKey;
    private String captureIdempotencyKey;
    private String refundReason;
    private BigDecimal capturedAmount;
    private BigDecimal refundedAmount;
    private final String processorReference;

    private final transient List<PaymentDomainEvent> domainEvents = new ArrayList<>();

    private Charge(UUID id,
                   UUID bookingId,
                   Long customerId,
                   BigDecimal amount,
                   String currency,
                   ChargeStatus status,
                   Instant createdAt,
                   Instant updatedAt,
                   String idempotencyKey,
                   String captureIdempotencyKey,
                   String refundReason,
                   BigDecimal capturedAmount,
                   BigDecimal refundedAmount,
                   String processorReference) {
        this.id = Objects.requireNonNull(id, "id");
        this.bookingId = Objects.requireNonNull(bookingId, "bookingId");
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        this.amount = Objects.requireNonNull(amount, "amount").setScale(2, RoundingMode.HALF_UP);
        if (this.amount.signum() <= 0) {
            throw new PaymentDomainException("Amount must be greater than zero");
        }
        this.currency = Objects.requireNonNull(currency, "currency");
        if (currency.isBlank()) {
            throw new PaymentDomainException("Currency cannot be blank");
        }
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        this.captureIdempotencyKey = captureIdempotencyKey;
        this.refundReason = refundReason;
        this.capturedAmount = capturedAmount;
        this.refundedAmount = refundedAmount;
        this.processorReference = Objects.requireNonNull(processorReference, "processorReference");
    }

    public static Charge authorize(UUID bookingId,
                                   Long customerId,
                                   BigDecimal amount,
                                   String currency,
                                   String processorReference,
                                   String idempotencyKey,
                                   Instant createdAt) {
        Charge charge = new Charge(UUID.randomUUID(), bookingId, customerId, amount, currency, ChargeStatus.AUTHORIZED,
                createdAt, createdAt, idempotencyKey, null, null, null, null, processorReference);
        charge.domainEvents.add(new PaymentAuthorizedEvent(charge.id, bookingId, customerId, charge.amount, charge.currency,
                createdAt));
        return charge;
    }

    public static Charge restore(UUID id,
                                 UUID bookingId,
                                 Long customerId,
                                 BigDecimal amount,
                                 String currency,
                                 ChargeStatus status,
                                 Instant createdAt,
                                 Instant updatedAt,
                                 String idempotencyKey,
                                 String captureIdempotencyKey,
                                 String refundReason,
                                 BigDecimal capturedAmount,
                                 BigDecimal refundedAmount,
                                 String processorReference) {
        return new Charge(id, bookingId, customerId, amount, currency, status, createdAt, updatedAt, idempotencyKey,
                captureIdempotencyKey, refundReason, capturedAmount, refundedAmount, processorReference);
    }

    public boolean isOwnedBy(Long customerId) {
        return Objects.equals(this.customerId, customerId);
    }

    public UUID getId() {
        return id;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public ChargeStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getCaptureIdempotencyKey() {
        return captureIdempotencyKey;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public BigDecimal getCapturedAmount() {
        return capturedAmount;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public String getProcessorReference() {
        return processorReference;
    }

    public List<PaymentDomainEvent> pullDomainEvents() {
        List<PaymentDomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    public boolean capture(BigDecimal amount, String captureIdempotencyKey, Instant capturedAt) {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(captureIdempotencyKey, "captureIdempotencyKey");
        Objects.requireNonNull(capturedAt, "capturedAt");
        if (status == ChargeStatus.CAPTURED && captureIdempotencyKey.equals(this.captureIdempotencyKey)) {
            return false;
        }
        if (status != ChargeStatus.AUTHORIZED) {
            throw new PaymentDomainException("Only authorized charges can be captured");
        }
        if (amount.signum() <= 0) {
            throw new PaymentDomainException("Capture amount must be greater than zero");
        }
        this.status = ChargeStatus.CAPTURED;
        this.updatedAt = capturedAt;
        this.captureIdempotencyKey = captureIdempotencyKey;
        this.capturedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        this.domainEvents.add(new PaymentCapturedEvent(id, bookingId, this.capturedAmount, capturedAt));
        return true;
    }

    public void refund(BigDecimal amount, String reason, boolean force, Instant refundedAt) {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(refundedAt, "refundedAt");
        if (amount.signum() <= 0) {
            throw new PaymentDomainException("Refund amount must be greater than zero");
        }
        if (!force && status != ChargeStatus.CAPTURED) {
            throw new PaymentDomainException("Only captured charges can be refunded");
        }
        this.status = ChargeStatus.REFUNDED;
        this.updatedAt = refundedAt;
        this.refundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        this.refundReason = reason;
        this.domainEvents.add(new PaymentRefundedEvent(id, bookingId, this.refundedAmount, reason, refundedAt));
    }

    public boolean isRefunded() {
        return status == ChargeStatus.REFUNDED;
    }

    public void fail(Instant failedAt) {
        Objects.requireNonNull(failedAt, "failedAt");
        if (status == ChargeStatus.CAPTURED) {
            throw new PaymentDomainException("Captured charges cannot be released");
        }
        this.status = ChargeStatus.FAILED;
        this.updatedAt = failedAt;
    }
}
