package pe.upc.ridera.bikelab.iam.application.queries;

import jakarta.validation.constraints.NotNull;

/**
 * Este query encapsula la búsqueda de un usuario específico por su identificador.
 */ 
public class GetUserByIdQuery {

    @NotNull
    private final Long userId;

    public GetUserByIdQuery(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
