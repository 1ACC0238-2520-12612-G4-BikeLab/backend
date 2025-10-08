package pe.upc.ridera.bikelab.payments.interfaces.rest;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.upc.ridera.bikelab.configuration.OpenApiConfig;
import pe.upc.ridera.bikelab.payments.application.dto.ChargeResource;
import pe.upc.ridera.bikelab.payments.application.services.PaymentService;
import pe.upc.ridera.bikelab.payments.interfaces.rest.resources.AuthorizeChargeRequest;
import pe.upc.ridera.bikelab.payments.interfaces.rest.resources.CaptureChargeRequest;
import pe.upc.ridera.bikelab.payments.interfaces.rest.resources.RefundChargeRequest;

@RestController
@RequestMapping("/api/payments/charges")
@Validated
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(name = "BC: Payments")
public class PaymentChargesController {

    private final PaymentService paymentService;

    public PaymentChargesController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Autorizar un cargo", description = "Permite a un cliente reservar el monto asociado a una reserva.")
    @PostMapping("/authorize")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ChargeResource> authorize(@Valid @RequestBody AuthorizeChargeRequest request) {
        Long customerId = currentUserId();
        ChargeResource charge = paymentService.authorizeCharge(request.bookingId(), customerId, request.paymentMethodId(),
                request.amount(), request.currency(), request.idempotencyKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(charge);
    }

    @Operation(summary = "Capturar un cargo autorizado", description = "Confirma el cobro asociado al alquiler finalizado.")
    @PostMapping("/{chargeId}/capture")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<ChargeResource> capture(@PathVariable UUID chargeId,
                                                  @Valid @RequestBody CaptureChargeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = hasAuthority(authentication, "ROLE_ADMIN");
        Long actorId = isAdmin ? parseUserId(authentication) : currentUserId();
        ChargeResource charge = paymentService.captureCharge(chargeId, actorId, isAdmin, request.idempotencyKey());
        return ResponseEntity.ok(charge);
    }

    @Operation(summary = "Reembolsar un cargo capturado", description = "Gestiona devoluciones por soporte o administradores.")
    @PostMapping("/{chargeId}/refund")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPPORT')")
    public ResponseEntity<ChargeResource> refund(@PathVariable UUID chargeId,
                                                 @Valid @RequestBody RefundChargeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = hasAuthority(authentication, "ROLE_ADMIN");
        ChargeResource charge = paymentService.refundCharge(chargeId, request.amount(), request.reason(), isAdmin);
        return ResponseEntity.ok(charge);
    }

    private Long currentUserId() {
        return parseUserId(SecurityContextHolder.getContext().getAuthentication());
    }

    private Long parseUserId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(authority)) {
                return true;
            }
        }
        return false;
    }
}
