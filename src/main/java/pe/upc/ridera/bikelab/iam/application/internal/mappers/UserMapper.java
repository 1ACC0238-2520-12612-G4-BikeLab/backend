package pe.upc.ridera.bikelab.iam.application.internal.mappers;

import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;

/**
 * Este mapeador transforma entidades de usuario en recursos expuestos por la capa de aplicación.
 */ 
public final class UserMapper {

    private UserMapper() {
    }

    public static UserResource toResource(User user) {
        return new UserResource(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoleNames());
    }
}
