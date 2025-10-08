package pe.upc.ridera.bikelab.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.upc.ridera.bikelab.iam.domain.model.entities.Role;
import pe.upc.ridera.bikelab.iam.domain.model.valueobjects.RoleName;

/**
 * Este repositorio JPA administra la persistencia de roles y búsquedas por nombre.
 */ 
public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
