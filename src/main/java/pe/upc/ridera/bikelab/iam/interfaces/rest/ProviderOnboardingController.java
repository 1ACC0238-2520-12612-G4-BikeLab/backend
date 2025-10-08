package pe.upc.ridera.bikelab.iam.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.upc.ridera.bikelab.iam.application.commands.OnboardProviderCommand;
import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.services.UserCommandService;

/**
 * Este controlador permite a los usuarios autenticados convertirse en proveedores.
 */ 
@RestController
@RequestMapping("/api/iam/providers")
@Tag(name = "BC: IAM")
public class ProviderOnboardingController {

    private final UserCommandService userCommandService;

    public ProviderOnboardingController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @Operation(summary = "Convertirme en proveedor",
            description = "Actualiza el perfil del usuario autenticado para habilitarlo como proveedor en la plataforma.")
    @PostMapping("/onboard")
    public ResponseEntity<UserResource> onboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        UserResource resource = userCommandService.handle(new OnboardProviderCommand(userId));
        return ResponseEntity.ok(resource);
    }
}
