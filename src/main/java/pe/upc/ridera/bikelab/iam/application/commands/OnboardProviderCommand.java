package pe.upc.ridera.bikelab.iam.application.commands;

import jakarta.validation.constraints.NotNull;

/**
 * Este comando habilita a un usuario existente para operar como proveedor en la plataforma.
 */ 
public class OnboardProviderCommand {

    @NotNull
    private final Long userId;

    public OnboardProviderCommand(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
