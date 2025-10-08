package pe.upc.ridera.bikelab.iam.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import pe.upc.ridera.bikelab.iam.domain.model.entities.Role;
import pe.upc.ridera.bikelab.iam.domain.model.valueobjects.RoleName;
import pe.upc.ridera.bikelab.iam.domain.persistence.RoleRepository;
import pe.upc.ridera.bikelab.iam.infrastructure.persistence.jpa.repositories.RoleJpaRepository;

/**
 * Este adaptador conecta la abstracción de roles con la implementación basada en JPA.
 */ 
@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryImpl(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleJpaRepository.findByName(name);
    }
}
