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

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Payout;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.PayoutStatus;

@Entity
@Table(name = "payouts")
public class PayoutEntity {

    @Id
    private UUID id;

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayoutStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public PayoutEntity() {
    }

    public static PayoutEntity fromAggregate(Payout payout) {
        PayoutEntity entity = new PayoutEntity();
        entity.id = payout.getId();
        entity.providerId = payout.getProviderId();
        entity.amount = payout.getAmount();
        entity.status = payout.getStatus();
        entity.createdAt = payout.getCreatedAt();
        entity.updatedAt = payout.getUpdatedAt();
        return entity;
    }

    public Payout toAggregate() {
        return Payout.restore(id, providerId, amount, status, createdAt, updatedAt);
    }

    public UUID getId() {
        return id;
    }
}
