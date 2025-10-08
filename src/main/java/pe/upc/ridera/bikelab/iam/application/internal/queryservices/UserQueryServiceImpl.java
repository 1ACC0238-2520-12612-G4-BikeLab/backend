package pe.upc.ridera.bikelab.iam.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.internal.mappers.UserMapper;
import pe.upc.ridera.bikelab.iam.application.queries.GetUserByIdQuery;
import pe.upc.ridera.bikelab.iam.application.queries.ListUsersQuery;
import pe.upc.ridera.bikelab.iam.application.services.UserQueryService;
import pe.upc.ridera.bikelab.iam.domain.persistence.UserRepository;

/**
 * Este servicio resuelve las consultas de usuarios transformando entidades a recursos de lectura.
 */ 
@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserResource> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.getUserId()).map(UserMapper::toResource);
    }

    @Override
    public List<UserResource> handle(ListUsersQuery query) {
        return userRepository.findAll().stream().map(UserMapper::toResource).toList();
    }
}
