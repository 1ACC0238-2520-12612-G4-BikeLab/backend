package pe.upc.ridera.bikelab.payments.interfaces.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import pe.upc.ridera.bikelab.payments.application.dto.PaymentMethodResource;
import pe.upc.ridera.bikelab.payments.application.services.PaymentService;
import pe.upc.ridera.bikelab.payments.interfaces.rest.resources.AddPaymentMethodRequest;

@RestController
@RequestMapping("/api/payments/methods")
@Validated
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(name = "BC: Payments")
public class PaymentMethodsController {

    private final PaymentService paymentService;

    public PaymentMethodsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Listar métodos de pago del cliente")
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<PaymentMethodResource>> list() {
        Long customerId = currentUserId();
        return ResponseEntity.ok(paymentService.listPaymentMethods(customerId));
    }

    @Operation(summary = "Registrar un nuevo método de pago")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<PaymentMethodResource> add(@Valid @RequestBody AddPaymentMethodRequest request) {
        Long customerId = currentUserId();
        PaymentMethodResource method = paymentService.addPaymentMethod(customerId, request.tokenRef(), request.brand(),
                request.last4(), request.makeDefault());
        return ResponseEntity.status(HttpStatus.CREATED).body(method);
    }

    @Operation(summary = "Eliminar un método de pago existente")
    @DeleteMapping("/{methodId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Void> remove(@PathVariable UUID methodId) {
        Long customerId = currentUserId();
        paymentService.removePaymentMethod(customerId, methodId);
        return ResponseEntity.noContent().build();
    }

    private Long currentUserId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
