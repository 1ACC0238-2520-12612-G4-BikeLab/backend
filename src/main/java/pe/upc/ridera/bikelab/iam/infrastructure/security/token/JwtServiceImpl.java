package pe.upc.ridera.bikelab.iam.infrastructure.security.token;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import pe.upc.ridera.bikelab.configuration.JwtProperties;
import pe.upc.ridera.bikelab.iam.application.dto.TokenResult;
import pe.upc.ridera.bikelab.iam.application.internal.outboundservices.tokens.JwtService;
import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;

/**
 * Esta implementación genera tokens JWT firmados utilizando la configuración del sistema.
 */ 
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtServiceImpl(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = resolveSecretKey(properties.getSecret());
    }

    @Override
    public TokenResult generateToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(properties.getExpiration());
        List<String> roles = user.getRoleNames();
        String token = Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuer(properties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("roles", roles)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        return new TokenResult(token, expiration);
    }

    private SecretKey resolveSecretKey(String secret) {
        try {
            byte[] decoded = Decoders.BASE64.decode(secret);
            return new SecretKeySpec(decoded, "HmacSHA256");
        } catch (IllegalArgumentException ex) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }
}
