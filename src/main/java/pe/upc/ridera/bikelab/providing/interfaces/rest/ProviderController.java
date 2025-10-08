package pe.upc.ridera.bikelab.providing.interfaces.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.upc.ridera.bikelab.configuration.OpenApiConfig;
import pe.upc.ridera.bikelab.providing.application.commands.ApproveProviderCommand;
import pe.upc.ridera.bikelab.providing.application.commands.RejectProviderCommand;
import pe.upc.ridera.bikelab.providing.application.commands.RequestOnboardingCommand;
import pe.upc.ridera.bikelab.providing.application.commands.SubmitKycCommand;
import pe.upc.ridera.bikelab.providing.application.dto.ProviderResource;
import pe.upc.ridera.bikelab.providing.application.queries.GetProviderByUserIdQuery;
import pe.upc.ridera.bikelab.providing.application.queries.ListProvidersQuery;
import pe.upc.ridera.bikelab.providing.application.services.ProviderCommandService;
import pe.upc.ridera.bikelab.providing.application.services.ProviderQueryService;
import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;
import pe.upc.ridera.bikelab.providing.infrastructure.security.SecurityContextAuthenticatedUserProvider;
import pe.upc.ridera.bikelab.providing.interfaces.rest.resources.ProviderDecisionRequest;
import pe.upc.ridera.bikelab.providing.interfaces.rest.resources.RequestOnboardingRequest;
import pe.upc.ridera.bikelab.providing.interfaces.rest.resources.SubmitKycRequest;

@RestController
@RequestMapping("/api/providing")
@Validated
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(name = "BC: Providing")
public class ProviderController {

    private final ProviderCommandService providerCommandService;
    private final ProviderQueryService providerQueryService;
    private final SecurityContextAuthenticatedUserProvider authenticatedUserProvider;

    public ProviderController(ProviderCommandService providerCommandService,
            ProviderQueryService providerQueryService,
            SecurityContextAuthenticatedUserProvider authenticatedUserProvider) {
        this.providerCommandService = providerCommandService;
        this.providerQueryService = providerQueryService;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Operation(summary = "Iniciar onboarding de proveedor")
    @PostMapping("/onboarding")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_PROVIDER')")
    public ProviderResource requestOnboarding(@Valid @RequestBody RequestOnboardingRequest request) {
        UUID userId = authenticatedUserProvider.getCurrentUserId();
        var command = new RequestOnboardingCommand(userId, request.displayName(), request.phone(), request.docType(),
                request.docNumber());
        return providerCommandService.requestOnboarding(command);
    }

    @Operation(summary = "Enviar información KYC")
    @PostMapping("/kyc")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ProviderResource submitKyc(@Valid @RequestBody SubmitKycRequest request) {
        UUID userId = authenticatedUserProvider.getCurrentUserId();
        var command = new SubmitKycCommand(userId, request.displayName(), request.phone(), request.docType(),
                request.docNumber());
        return providerCommandService.submitKyc(command);
    }

    @Operation(summary = "Obtener mi proveedor")
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ResponseEntity<ProviderResource> getMyProvider() {
        UUID userId = authenticatedUserProvider.getCurrentUserId();
        return providerQueryService.getProvider(new GetProviderByUserIdQuery(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar proveedores")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SUPPORT','ROLE_ADMIN')")
    public List<ProviderResource> listProviders(@RequestParam(required = false) ProviderStatus status,
            @RequestParam(required = false) String search) {
        Optional<String> searchFilter = Optional.ofNullable(search)
                .map(String::trim)
                .filter(value -> !value.isEmpty());
        ListProvidersQuery query = new ListProvidersQuery(Optional.ofNullable(status), searchFilter);
        return providerQueryService.listProviders(query);
    }

    @Operation(summary = "Aprobar un proveedor")
    @PostMapping("/{providerId}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPPORT')")
    public ProviderResource approve(@PathVariable UUID providerId,
            @Valid @RequestBody ProviderDecisionRequest request) {
        UUID adminId = authenticatedUserProvider.getCurrentUserId();
        var command = new ApproveProviderCommand(adminId, providerId, request.reason());
        return providerCommandService.approveProvider(command);
    }

    @Operation(summary = "Rechazar un proveedor")
    @PostMapping("/{providerId}/reject")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPPORT')")
    public ProviderResource reject(@PathVariable UUID providerId,
            @Valid @RequestBody ProviderDecisionRequest request) {
        UUID adminId = authenticatedUserProvider.getCurrentUserId();
        var command = new RejectProviderCommand(adminId, providerId, request.reason());
        return providerCommandService.rejectProvider(command);
    }
}
