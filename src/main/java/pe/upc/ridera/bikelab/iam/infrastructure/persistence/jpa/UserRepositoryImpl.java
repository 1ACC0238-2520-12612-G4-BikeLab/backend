package pe.upc.ridera.bikelab.iam.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;
import pe.upc.ridera.bikelab.iam.domain.persistence.UserRepository;
import pe.upc.ridera.bikelab.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;

/**
 * Este adaptador delega las operaciones del repositorio de usuarios a JPA.
 */ 
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }
}
