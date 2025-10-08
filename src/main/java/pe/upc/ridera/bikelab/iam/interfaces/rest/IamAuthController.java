package pe.upc.ridera.bikelab.iam.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.upc.ridera.bikelab.iam.application.commands.AuthenticateUserCommand;
import pe.upc.ridera.bikelab.iam.application.commands.RegisterUserCommand;
import pe.upc.ridera.bikelab.iam.application.dto.AuthResponse;
import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.services.UserCommandService;
import pe.upc.ridera.bikelab.iam.interfaces.rest.resources.LoginResource;
import pe.upc.ridera.bikelab.iam.interfaces.rest.resources.RegisterUserResource;

/**
 * Este controlador administra el registro y autenticación de usuarios mediante JWT.
 */ 
@RestController
@RequestMapping("/api/iam")
@Tag(name = "BC: IAM")
public class IamAuthController {

    private final UserCommandService userCommandService;

    public IamAuthController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Crea una cuenta en la plataforma y devuelve el perfil básico del usuario registrado.")
    @PostMapping("/auth/register")
    public ResponseEntity<UserResource> register(@Valid @RequestBody RegisterUserResource resource) {
        RegisterUserCommand command = new RegisterUserCommand(
                resource.getEmail(),
                resource.getPassword(),
                resource.getFirstName(),
                resource.getLastName());
        try {
            UserResource user = userCommandService.handle(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
        }
    }

    @Operation(summary = "Autenticar un usuario",
            description = "Valida las credenciales ingresadas y retorna un token JWT para acceder a los demás servicios.")
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginResource resource) {
        AuthenticateUserCommand command = new AuthenticateUserCommand(resource.getEmail(), resource.getPassword());
        try {
            AuthResponse response = userCommandService.handle(command);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        }
    }
}
