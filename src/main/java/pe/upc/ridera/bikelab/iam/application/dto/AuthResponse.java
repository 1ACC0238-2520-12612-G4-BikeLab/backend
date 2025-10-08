package pe.upc.ridera.bikelab.iam.application.dto;

import java.time.Instant;

/**
 * Esta respuesta agrupa el token emitido y los datos del usuario autenticado.
 */ 
public class AuthResponse {

    private final String token;
    private final String tokenType;
    private final Instant expiresAt;
    private final UserResource user;

    public AuthResponse(String token, String tokenType, Instant expiresAt, UserResource user) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public UserResource getUser() {
        return user;
    }
}
