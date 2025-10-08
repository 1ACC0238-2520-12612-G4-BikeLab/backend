package pe.upc.ridera.bikelab.payments.domain.model.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Interfaz base para los eventos de dominio generados por el bounded context de pagos.
 */
public interface PaymentDomainEvent {

    UUID chargeId();

    Instant occurredOn();
}
