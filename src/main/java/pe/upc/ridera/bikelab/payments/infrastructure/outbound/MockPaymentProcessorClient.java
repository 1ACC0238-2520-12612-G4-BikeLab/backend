package pe.upc.ridera.bikelab.payments.infrastructure.outbound;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.payments.application.outbound.PaymentProcessorClient;

@Component
public class MockPaymentProcessorClient implements PaymentProcessorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockPaymentProcessorClient.class);

    private final Map<String, String> authorizationByKey = new ConcurrentHashMap<>();
    private final Set<String> capturedByKey = ConcurrentHashMap.newKeySet();
    private final Duration artificialLatency;

    public MockPaymentProcessorClient(@Value("${payments.psp.latency:PT0.25S}") Duration artificialLatency) {
        this.artificialLatency = artificialLatency;
    }

    @Override
    public AuthorizationResponse authorize(Long customerId,
                                           UUID bookingId,
                                           BigDecimal amount,
                                           String currency,
                                           String paymentToken,
                                           String idempotencyKey) {
        simulateLatency();
        String processorReference = authorizationByKey.computeIfAbsent(idempotencyKey,
                key -> UUID.randomUUID().toString());
        LOGGER.info("[PSP] Authorized {} {} for booking {} with token {}", currency, amount, bookingId, paymentToken);
        return new AuthorizationResponse(processorReference);
    }

    @Override
    public void capture(String processorReference, BigDecimal amount, String idempotencyKey) {
        simulateLatency();
        if (capturedByKey.add(idempotencyKey)) {
            LOGGER.info("[PSP] Captured {} for reference {}", amount, processorReference);
        } else {
            LOGGER.info("[PSP] Capture idempotent hit for reference {}", processorReference);
        }
    }

    @Override
    public void refund(String processorReference, BigDecimal amount, String reason) {
        simulateLatency();
        LOGGER.info("[PSP] Refunded {} for reference {} due to {}", amount, processorReference, reason);
    }

    private void simulateLatency() {
        if (artificialLatency.isZero()) {
            return;
        }
        try {
            Thread.sleep(artificialLatency.toMillis());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
