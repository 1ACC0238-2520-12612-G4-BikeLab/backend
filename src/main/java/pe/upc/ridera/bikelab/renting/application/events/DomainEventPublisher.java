package pe.upc.ridera.bikelab.renting.application.events;

import java.util.Collection;

import pe.upc.ridera.bikelab.renting.domain.model.events.BookingDomainEvent;

/**
 * Esta interfaz define el contrato para publicar eventos de dominio dentro del contexto de renting.
 */
public interface DomainEventPublisher {

    void publish(BookingDomainEvent event);

    default void publishAll(Collection<? extends BookingDomainEvent> events) {
        events.forEach(this::publish);
    }
}
