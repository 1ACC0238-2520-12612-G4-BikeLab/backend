package pe.upc.ridera.bikelab.iam.application.dto;

import java.time.Instant;

/**
 * Este valor de retorno encapsula el token generado y su expiración.
 */ 
public class TokenResult {

    private final String token;
    private final Instant expiresAt;

    public TokenResult(String token, Instant expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
