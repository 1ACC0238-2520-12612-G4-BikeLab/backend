package pe.upc.ridera.bikelab.renting.infrastructure.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentAuthorizedEvent;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentCapturedEvent;
import pe.upc.ridera.bikelab.payments.domain.model.events.PaymentRefundedEvent;

@Component
public class PaymentEventsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventsListener.class);

    @EventListener
    public void onPaymentAuthorized(PaymentAuthorizedEvent event) {
        LOGGER.info("Received payment authorization event for booking {}", event.bookingId());
    }

    @EventListener
    public void onPaymentCaptured(PaymentCapturedEvent event) {
        LOGGER.info("Received payment captured event for booking {}", event.bookingId());
    }

    @EventListener
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        LOGGER.info("Received payment refunded event for booking {}", event.bookingId());
    }
}
