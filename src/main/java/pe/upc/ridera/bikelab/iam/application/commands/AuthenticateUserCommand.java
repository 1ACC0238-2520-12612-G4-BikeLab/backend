package pe.upc.ridera.bikelab.iam.application.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Este comando encapsula las credenciales para autenticar a un usuario.
 */ 
public class AuthenticateUserCommand {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private final String password;

    public AuthenticateUserCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
