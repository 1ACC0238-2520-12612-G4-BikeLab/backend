package pe.upc.ridera.bikelab.providing.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Servicio que expone la información del usuario autenticado a partir del JWT emitido por IAM.
 */
@Component
public class SecurityContextAuthenticatedUserProvider {

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No hay un usuario autenticado en el contexto");
        }
        UUID userId = resolveUserId(authentication);
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new AuthenticatedUser(userId, roles);
    }

    public UUID resolveUserId(Authentication authentication) {
        String principal = authentication.getName();
        return UUID.nameUUIDFromBytes(principal.getBytes(StandardCharsets.UTF_8));
    }

    public UUID getCurrentUserId() {
        return getAuthenticatedUser().userId();
    }

    public boolean hasRole(String role) {
        return getAuthenticatedUser().roles().stream()
                .anyMatch(r -> r.equals(role));
    }
}
