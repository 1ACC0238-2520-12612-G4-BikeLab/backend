package pe.upc.ridera.bikelab.payments.application.events;

import java.util.List;

import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentDomainEvent;

/**
 * Puerto para publicar eventos de dominio del contexto de pagos.
 */
public interface PaymentEventPublisher {

    void publish(PaymentDomainEvent event);

    default void publishAll(List<PaymentDomainEvent> events) {
        events.forEach(this::publish);
    }
}
