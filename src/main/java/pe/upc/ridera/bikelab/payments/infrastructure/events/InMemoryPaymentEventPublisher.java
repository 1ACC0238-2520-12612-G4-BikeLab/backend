package pe.upc.ridera.bikelab.payments.infrastructure.events;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.payments.application.events.PaymentEventPublisher;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentDomainEvent;

@Component
public class InMemoryPaymentEventPublisher implements PaymentEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryPaymentEventPublisher.class);

    private final List<PaymentDomainEvent> outbox = new LinkedList<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemoryPaymentEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public synchronized void publish(PaymentDomainEvent event) {
        Objects.requireNonNull(event, "event");
        outbox.add(event);
        LOGGER.info("Payment domain event published: {}", event);
        applicationEventPublisher.publishEvent(event);
    }

    public synchronized List<PaymentDomainEvent> getPublishedEvents() {
        return Collections.unmodifiableList(outbox);
    }

    public synchronized void clear() {
        outbox.clear();
    }
}
