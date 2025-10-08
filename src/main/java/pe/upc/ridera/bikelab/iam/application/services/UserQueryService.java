package pe.upc.ridera.bikelab.iam.application.services;

import java.util.List;
import java.util.Optional;

import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.queries.GetUserByIdQuery;
import pe.upc.ridera.bikelab.iam.application.queries.ListUsersQuery;

/**
 * Este servicio gestiona consultas de lectura sobre los usuarios registrados.
 */ 
public interface UserQueryService {

    Optional<UserResource> handle(GetUserByIdQuery query);

    List<UserResource> handle(ListUsersQuery query);
}
