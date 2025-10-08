package pe.upc.ridera.bikelab.iam.domain.persistence;

import java.util.Optional;

import pe.upc.ridera.bikelab.iam.domain.model.entities.Role;
import pe.upc.ridera.bikelab.iam.domain.model.valueobjects.RoleName;

/**
 * Este repositorio ofrece búsquedas de roles por su nombre dentro del contexto IAM.
 */ 
public interface RoleRepository {

    Optional<Role> findByName(RoleName name);
}
