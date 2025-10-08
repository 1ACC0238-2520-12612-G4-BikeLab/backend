package pe.upc.ridera.bikelab.iam.application.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Este comando reúne los datos necesarios para registrar un nuevo usuario.
 */ 
public class RegisterUserCommand {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private final String password;

    @NotBlank
    @Size(max = 100)
    private final String firstName;

    @NotBlank
    @Size(max = 100)
    private final String lastName;

    public RegisterUserCommand(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
