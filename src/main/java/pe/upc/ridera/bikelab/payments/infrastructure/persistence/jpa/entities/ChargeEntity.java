package pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Charge;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;

@Entity
@Table(name = "charges")
public class ChargeEntity {

    @Id
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChargeStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "capture_idempotency_key")
    private String captureIdempotencyKey;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "captured_amount")
    private BigDecimal capturedAmount;

    @Column(name = "refunded_amount")
    private BigDecimal refundedAmount;

    @Column(name = "processor_reference", nullable = false)
    private String processorReference;

    public ChargeEntity() {
    }

    public static ChargeEntity fromAggregate(Charge charge) {
        ChargeEntity entity = new ChargeEntity();
        entity.id = charge.getId();
        entity.bookingId = charge.getBookingId();
        entity.customerId = charge.getCustomerId();
        entity.amount = charge.getAmount();
        entity.currency = charge.getCurrency();
        entity.status = charge.getStatus();
        entity.createdAt = charge.getCreatedAt();
        entity.updatedAt = charge.getUpdatedAt();
        entity.idempotencyKey = charge.getIdempotencyKey();
        entity.captureIdempotencyKey = charge.getCaptureIdempotencyKey();
        entity.refundReason = charge.getRefundReason();
        entity.capturedAmount = charge.getCapturedAmount();
        entity.refundedAmount = charge.getRefundedAmount();
        entity.processorReference = charge.getProcessorReference();
        return entity;
    }

    public Charge toAggregate() {
        return Charge.restore(id, bookingId, customerId, amount, currency, status, createdAt, updatedAt, idempotencyKey,
                captureIdempotencyKey, refundReason, capturedAmount, refundedAmount, processorReference);
    }

    public UUID getId() {
        return id;
    }
}
