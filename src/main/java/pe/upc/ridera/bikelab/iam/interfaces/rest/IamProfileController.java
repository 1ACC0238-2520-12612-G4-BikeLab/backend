package pe.upc.ridera.bikelab.iam.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.queries.GetUserByIdQuery;
import pe.upc.ridera.bikelab.iam.application.services.UserQueryService;

/**
 * Este controlador devuelve la información del usuario autenticado.
 */ 
@RestController
@RequestMapping("/api/iam")
@Tag(name = "BC: IAM")
public class IamProfileController {

    private final UserQueryService userQueryService;

    public IamProfileController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Operation(summary = "Consultar mi perfil",
            description = "Devuelve la información del usuario autenticado a partir del identificador del token.")
    @GetMapping("/me")
    public ResponseEntity<UserResource> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
