package pe.upc.ridera.bikelab.iam.interfaces.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.queries.ListUsersQuery;
import pe.upc.ridera.bikelab.iam.application.services.UserQueryService;

/**
 * Este controlador expone operaciones administrativas para consultar usuarios registrados.
 */ 
@RestController
@RequestMapping("/api/admin")
@Tag(name = "BC: IAM")
public class AdminUserController {

    private final UserQueryService userQueryService;

    public AdminUserController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Operation(summary = "Listar usuarios registrados",
            description = "Entrega un listado de usuarios para fines de supervisión y soporte.")
    @GetMapping("/users")
    public ResponseEntity<List<UserResource>> listUsers() {
        List<UserResource> users = userQueryService.handle(new ListUsersQuery());
        return ResponseEntity.ok(users);
    }
}
