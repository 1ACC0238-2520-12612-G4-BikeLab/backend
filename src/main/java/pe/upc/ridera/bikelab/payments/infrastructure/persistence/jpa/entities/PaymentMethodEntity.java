package pe.upc.ridera.bikelab.payments.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.model.aggregates.PaymentMethod;

@Entity
@Table(name = "payment_methods")
public class PaymentMethodEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "brand", nullable = false)
    private String brand;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "last4", nullable = false, length = 4, columnDefinition = "char(4)")
    private String last4;

    @Column(name = "token_ref", nullable = false)
    private String tokenRef;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public PaymentMethodEntity() {
    }

    public static PaymentMethodEntity fromAggregate(PaymentMethod method) {
        PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.id = method.getId();
        entity.customerId = method.getCustomerId();
        entity.brand = method.getBrand();
        entity.last4 = method.getLast4();
        entity.tokenRef = method.getTokenRef();
        entity.isDefault = method.isDefault();
        entity.createdAt = method.getCreatedAt();
        return entity;
    }

    public PaymentMethod toAggregate() {
        return PaymentMethod.restore(id, customerId, brand, last4, tokenRef, isDefault, createdAt);
    }

    public UUID getId() {
        return id;
    }
}
