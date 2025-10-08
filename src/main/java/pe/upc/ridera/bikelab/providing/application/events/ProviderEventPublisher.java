package pe.upc.ridera.bikelab.providing.application.events;

import pe.upc.ridera.bikelab.providing.domain.model.events.ProviderApprovedDomainEvent;

/**
 * Publicador de eventos del dominio de proveedores.
 */
public interface ProviderEventPublisher {

    void publish(ProviderApprovedDomainEvent event);
}
