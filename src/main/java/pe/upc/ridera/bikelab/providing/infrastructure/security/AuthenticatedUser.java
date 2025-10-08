package pe.upc.ridera.bikelab.providing.infrastructure.security;

import java.util.List;
import java.util.UUID;

/**
 * Representa al usuario autenticado obtenido desde el contexto de seguridad.
 */
public record AuthenticatedUser(UUID userId, List<String> roles) {
}
