package pe.upc.ridera.bikelab.payments.interfaces.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.upc.ridera.bikelab.configuration.OpenApiConfig;
import pe.upc.ridera.bikelab.payments.application.dto.PayoutResource;
import pe.upc.ridera.bikelab.payments.application.services.PaymentService;

@RestController
@RequestMapping("/api/payments/payouts")
@Validated
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(name = "BC: Payments")
public class PayoutsController {

    private final PaymentService paymentService;

    public PayoutsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Listar mis pagos como proveedor")
    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ResponseEntity<List<PayoutResource>> listMine() {
        Long providerId = currentUserId();
        return ResponseEntity.ok(paymentService.listPayouts(providerId));
    }

    private Long currentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
