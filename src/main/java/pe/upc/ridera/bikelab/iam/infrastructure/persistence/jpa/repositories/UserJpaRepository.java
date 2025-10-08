package pe.upc.ridera.bikelab.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;

/**
 * Este repositorio JPA maneja las operaciones de persistencia para usuarios y búsquedas por correo.
 */ 
public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
