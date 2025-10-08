package pe.upc.ridera.bikelab.iam.application.internal.outboundservices.tokens;

import pe.upc.ridera.bikelab.iam.application.dto.TokenResult;
import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;

/**
 * Este puerto define la generación de tokens JWT para usuarios autenticados.
 */ 
public interface JwtService {

    TokenResult generateToken(User user);
}
