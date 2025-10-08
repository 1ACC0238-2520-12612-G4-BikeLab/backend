package pe.upc.ridera.bikelab.payments.application.internal;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.payments.application.dto.ChargeResource;
import pe.upc.ridera.bikelab.payments.application.dto.PaymentMethodResource;
import pe.upc.ridera.bikelab.payments.application.dto.PayoutResource;
import pe.upc.ridera.bikelab.payments.application.events.PaymentEventPublisher;
import pe.upc.ridera.bikelab.payments.application.outbound.PaymentProcessorClient;
import pe.upc.ridera.bikelab.payments.application.services.PaymentService;
import pe.upc.ridera.bikelab.payments.domain.exceptions.PaymentDomainException;
import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Charge;
import pe.upc.ridera.bikelab.payments.domain.model.aggregates.PaymentMethod;
import pe.upc.ridera.bikelab.payments.domain.model.aggregates.Payout;
import pe.upc.ridera.bikelab.payments.domain.persistence.ChargeRepository;
import pe.upc.ridera.bikelab.payments.domain.persistence.PaymentMethodRepository;
import pe.upc.ridera.bikelab.payments.domain.persistence.PayoutRepository;
import pe.upc.ridera.bikelab.payments.domain.model.valueobjects.ChargeStatus;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final ChargeRepository chargeRepository;
    private final PayoutRepository payoutRepository;
    private final PaymentProcessorClient paymentProcessorClient;
    private final PaymentEventPublisher paymentEventPublisher;
    private final Clock clock;

    public PaymentServiceImpl(PaymentMethodRepository paymentMethodRepository,
                              ChargeRepository chargeRepository,
                              PayoutRepository payoutRepository,
                              PaymentProcessorClient paymentProcessorClient,
                              PaymentEventPublisher paymentEventPublisher,
                              Clock clock) {
        this.paymentMethodRepository = Objects.requireNonNull(paymentMethodRepository);
        this.chargeRepository = Objects.requireNonNull(chargeRepository);
        this.payoutRepository = Objects.requireNonNull(payoutRepository);
        this.paymentProcessorClient = Objects.requireNonNull(paymentProcessorClient);
        this.paymentEventPublisher = Objects.requireNonNull(paymentEventPublisher);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public ChargeResource authorizeCharge(UUID bookingId,
                                          Long customerId,
                                          UUID paymentMethodId,
                                          BigDecimal amount,
                                          String currency,
                                          String idempotencyKey) {
        Objects.requireNonNull(bookingId, "bookingId");
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        if (currency.isBlank()) {
            throw new PaymentDomainException("Currency cannot be blank");
        }
        if (idempotencyKey.isBlank()) {
            throw new PaymentDomainException("Idempotency key cannot be blank");
        }
        String normalizedCurrency = currency.toUpperCase(Locale.ROOT);

        Optional<Charge> existing = chargeRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return toResource(existing.get());
        }

        PaymentMethod method = resolvePaymentMethod(customerId, paymentMethodId);
        Instant now = Instant.now(clock);
        var authorization = paymentProcessorClient.authorize(customerId, bookingId, amount, normalizedCurrency,
                method.getTokenRef(), idempotencyKey);
        Charge charge = Charge.authorize(bookingId, customerId, amount, normalizedCurrency,
                authorization.processorReference(), idempotencyKey, now);
        var events = charge.pullDomainEvents();
        Charge saved = chargeRepository.save(charge);
        paymentEventPublisher.publishAll(events);
        return toResource(saved);
    }

    @Override
    public ChargeResource captureCharge(UUID chargeId, Long actorId, boolean overrideOwnership, String idempotencyKey) {
        Objects.requireNonNull(chargeId, "chargeId");
        Objects.requireNonNull(idempotencyKey, "idempotencyKey");
        if (idempotencyKey.isBlank()) {
            throw new PaymentDomainException("Idempotency key cannot be blank");
        }
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new PaymentDomainException("Charge not found"));
        if (!overrideOwnership) {
            Objects.requireNonNull(actorId, "actorId");
            if (!charge.isOwnedBy(actorId)) {
                throw new PaymentDomainException("Customer is not allowed to capture this charge");
            }
        }
        if (charge.getStatus() == ChargeStatus.CAPTURED
                && idempotencyKey.equals(charge.getCaptureIdempotencyKey())) {
            return toResource(charge);
        }
        if (charge.getStatus() != ChargeStatus.AUTHORIZED) {
            throw new PaymentDomainException("Only authorized charges can be captured");
        }
        paymentProcessorClient.capture(charge.getProcessorReference(), charge.getAmount(), idempotencyKey);
        charge.capture(charge.getAmount(), idempotencyKey, Instant.now(clock));
        var events = charge.pullDomainEvents();
        Charge saved = chargeRepository.save(charge);
        paymentEventPublisher.publishAll(events);
        return toResource(saved);
    }

    @Override
    public ChargeResource refundCharge(UUID chargeId, BigDecimal amount, String reason, boolean force) {
        Objects.requireNonNull(chargeId, "chargeId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(reason, "reason");
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new PaymentDomainException("Charge not found"));
        if (charge.isRefunded()) {
            return toResource(charge);
        }
        if (reason.isBlank()) {
            throw new PaymentDomainException("Refund reason cannot be blank");
        }
        paymentProcessorClient.refund(charge.getProcessorReference(), amount, reason);
        charge.refund(amount, reason, force, Instant.now(clock));
        var events = charge.pullDomainEvents();
        Charge saved = chargeRepository.save(charge);
        paymentEventPublisher.publishAll(events);
        return toResource(saved);
    }

    @Override
    public PaymentMethodResource addPaymentMethod(Long customerId,
                                                  String tokenRef,
                                                  String brand,
                                                  String last4,
                                                  boolean makeDefault) {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(tokenRef, "tokenRef");
        Objects.requireNonNull(brand, "brand");
        Objects.requireNonNull(last4, "last4");
        Instant now = Instant.now(clock);
        PaymentMethod method = PaymentMethod.create(customerId, tokenRef, brand, last4, makeDefault, now);
        List<PaymentMethod> customerMethods = paymentMethodRepository.findByCustomerId(customerId);
        if (makeDefault) {
            customerMethods.stream()
                    .filter(PaymentMethod::isDefault)
                    .forEach(existing -> {
                        existing.removeDefault();
                        paymentMethodRepository.save(existing);
                    });
        } else if (customerMethods.isEmpty()) {
            method.markAsDefault();
        }
        PaymentMethod saved = paymentMethodRepository.save(method);
        return toResource(saved);
    }

    @Override
    public void removePaymentMethod(Long customerId, UUID methodId) {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(methodId, "methodId");
        PaymentMethod method = paymentMethodRepository.findById(methodId)
                .orElseThrow(() -> new PaymentDomainException("Payment method not found"));
        if (!Objects.equals(method.getCustomerId(), customerId)) {
            throw new PaymentDomainException("Customer cannot remove a payment method from another account");
        }
        paymentMethodRepository.delete(method);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodResource> listPaymentMethods(Long customerId) {
        Objects.requireNonNull(customerId, "customerId");
        return paymentMethodRepository.findByCustomerId(customerId).stream()
                .sorted(Comparator.comparing(PaymentMethod::isDefault).reversed()
                        .thenComparing(PaymentMethod::getCreatedAt))
                .map(this::toResource)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayoutResource> listPayouts(Long providerId) {
        Objects.requireNonNull(providerId, "providerId");
        return payoutRepository.findByProviderId(providerId).stream()
                .sorted(Comparator.comparing(Payout::getCreatedAt).reversed())
                .map(this::toResource)
                .toList();
    }

    private PaymentMethod resolvePaymentMethod(Long customerId, UUID paymentMethodId) {
        List<PaymentMethod> customerMethods = paymentMethodRepository.findByCustomerId(customerId);
        if (customerMethods.isEmpty()) {
            throw new PaymentDomainException("Customer has no payment methods configured");
        }
        if (paymentMethodId != null) {
            return customerMethods.stream()
                    .filter(method -> method.getId().equals(paymentMethodId))
                    .findFirst()
                    .orElseThrow(() -> new PaymentDomainException("Payment method not found"));
        }
        return customerMethods.stream()
                .filter(PaymentMethod::isDefault)
                .findFirst()
                .orElse(customerMethods.get(0));
    }

    private ChargeResource toResource(Charge charge) {
        return new ChargeResource(charge.getId(),
                charge.getBookingId(),
                charge.getCustomerId(),
                charge.getAmount(),
                charge.getCurrency(),
                charge.getStatus(),
                charge.getCapturedAmount(),
                charge.getRefundedAmount(),
                charge.getCreatedAt(),
                charge.getUpdatedAt());
    }

    private PaymentMethodResource toResource(PaymentMethod method) {
        return new PaymentMethodResource(method.getId(), method.getBrand(), method.getLast4(), method.isDefault(),
                method.getCreatedAt());
    }

    private PayoutResource toResource(Payout payout) {
        return new PayoutResource(payout.getId(), payout.getProviderId(), payout.getAmount(), payout.getStatus(),
                payout.getCreatedAt(), payout.getUpdatedAt());
    }

    @Override
    public void releaseAuthorization(UUID chargeId) {
        Objects.requireNonNull(chargeId, "chargeId");
        chargeRepository.findById(chargeId).ifPresent(charge -> {
            if (charge.getStatus() == ChargeStatus.AUTHORIZED) {
                charge.fail(Instant.now(clock));
                chargeRepository.save(charge);
            }
        });
    }
}
