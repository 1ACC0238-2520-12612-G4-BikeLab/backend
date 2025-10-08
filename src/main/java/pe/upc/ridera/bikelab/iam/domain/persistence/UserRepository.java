package pe.upc.ridera.bikelab.iam.domain.persistence;

import java.util.List;
import java.util.Optional;

import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;

/**
 * Este repositorio abstrae el acceso a los usuarios persistidos para el contexto IAM.
 */ 
public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAll();

    long count();
}
