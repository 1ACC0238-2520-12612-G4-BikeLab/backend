package pe.upc.ridera.bikelab.iam.application.dto;

import java.util.List;

/**
 * Este recurso expone los datos esenciales de un usuario para respuestas REST del IAM.
 */ 
public class UserResource {

    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final List<String> roles;

    public UserResource(Long id, String email, String firstName, String lastName, List<String> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getRoles() {
        return roles;
    }
}
