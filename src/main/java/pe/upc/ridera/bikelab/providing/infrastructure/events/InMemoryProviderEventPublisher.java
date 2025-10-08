package pe.upc.ridera.bikelab.providing.infrastructure.events;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.providing.application.events.ProviderEventPublisher;
import pe.upc.ridera.bikelab.providing.domain.model.events.ProviderApprovedDomainEvent;

/**
 * Publicador de eventos en memoria que reenvía los eventos al ApplicationEventPublisher de Spring.
 */
@Component
public class InMemoryProviderEventPublisher implements ProviderEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryProviderEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<ProviderApprovedDomainEvent> outbox = new LinkedList<>();

    public InMemoryProviderEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(ProviderApprovedDomainEvent event) {
        Objects.requireNonNull(event, "event");
        outbox.add(event);
        LOGGER.info("Provider approved event published: {}", event);
        applicationEventPublisher.publishEvent(event);
    }

    public List<ProviderApprovedDomainEvent> getPublishedEvents() {
        return List.copyOf(outbox);
    }
}
