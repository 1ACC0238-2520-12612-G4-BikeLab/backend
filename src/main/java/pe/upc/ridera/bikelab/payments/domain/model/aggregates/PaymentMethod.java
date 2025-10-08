package pe.upc.ridera.bikelab.payments.domain.model.aggregates;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import pe.upc.ridera.bikelab.payments.domain.exceptions.PaymentDomainException;

/**
 * Agregado responsable de gestionar los métodos de pago registrados por un cliente.
 */
public class PaymentMethod {

    private final UUID id;
    private final Long customerId;
    private final String brand;
    private final String last4;
    private final String tokenRef;
    private boolean isDefault;
    private final Instant createdAt;

    private PaymentMethod(UUID id,
                          Long customerId,
                          String brand,
                          String last4,
                          String tokenRef,
                          boolean isDefault,
                          Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        this.brand = Objects.requireNonNull(brand, "brand");
        this.last4 = Objects.requireNonNull(last4, "last4");
        if (last4.length() != 4) {
            throw new PaymentDomainException("Last4 must contain 4 digits");
        }
        this.tokenRef = Objects.requireNonNull(tokenRef, "tokenRef");
        this.isDefault = isDefault;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public static PaymentMethod create(Long customerId,
                                       String tokenRef,
                                       String brand,
                                       String last4,
                                       boolean isDefault,
                                       Instant createdAt) {
        if (tokenRef.isBlank()) {
            throw new PaymentDomainException("Token reference cannot be blank");
        }
        if (brand.isBlank()) {
            throw new PaymentDomainException("Brand cannot be blank");
        }
        return new PaymentMethod(UUID.randomUUID(), customerId, brand, last4, tokenRef, isDefault, createdAt);
    }

    public static PaymentMethod restore(UUID id,
                                        Long customerId,
                                        String brand,
                                        String last4,
                                        String tokenRef,
                                        boolean isDefault,
                                        Instant createdAt) {
        return new PaymentMethod(id, customerId, brand, last4, tokenRef, isDefault, createdAt);
    }

    public void markAsDefault() {
        this.isDefault = true;
    }

    public void removeDefault() {
        this.isDefault = false;
    }

    public UUID getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getBrand() {
        return brand;
    }

    public String getLast4() {
        return last4;
    }

    public String getTokenRef() {
        return tokenRef;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
