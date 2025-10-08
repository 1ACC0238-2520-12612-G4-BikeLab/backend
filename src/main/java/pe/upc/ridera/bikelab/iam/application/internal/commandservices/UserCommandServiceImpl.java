package pe.upc.ridera.bikelab.iam.application.internal.commandservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.iam.application.commands.AuthenticateUserCommand;
import pe.upc.ridera.bikelab.iam.application.commands.OnboardProviderCommand;
import pe.upc.ridera.bikelab.iam.application.commands.RegisterUserCommand;
import pe.upc.ridera.bikelab.iam.application.dto.AuthResponse;
import pe.upc.ridera.bikelab.iam.application.dto.TokenResult;
import pe.upc.ridera.bikelab.iam.application.dto.UserResource;
import pe.upc.ridera.bikelab.iam.application.internal.mappers.UserMapper;
import pe.upc.ridera.bikelab.iam.application.internal.outboundservices.tokens.JwtService;
import pe.upc.ridera.bikelab.iam.application.services.UserCommandService;
import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;
import pe.upc.ridera.bikelab.iam.domain.model.entities.Role;
import pe.upc.ridera.bikelab.iam.domain.model.valueobjects.RoleName;
import pe.upc.ridera.bikelab.iam.domain.persistence.RoleRepository;
import pe.upc.ridera.bikelab.iam.domain.persistence.UserRepository;

/**
 * Este servicio implementa las operaciones de comandos para registro, autenticación y alta de proveedores.
 */ 
@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserCommandServiceImpl(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder,
                                  JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserResource handle(RegisterUserCommand command) {
        userRepository.findByEmail(command.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("Email is already registered");
        });
        Role defaultRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("Default role not configured"));
        User user = new User(
                command.getEmail(),
                passwordEncoder.encode(command.getPassword()),
                command.getFirstName(),
                command.getLastName());
        user.addRole(defaultRole);
        User saved = userRepository.save(user);
        return UserMapper.toResource(saved);
    }

    @Override
    public AuthResponse handle(AuthenticateUserCommand command) {
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        TokenResult tokenResult = jwtService.generateToken(user);
        return new AuthResponse(tokenResult.getToken(), "Bearer", tokenResult.getExpiresAt(), UserMapper.toResource(user));
    }

    @Override
    public UserResource handle(OnboardProviderCommand command) {
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role providerRole = roleRepository.findByName(RoleName.ROLE_PROVIDER)
                .orElseThrow(() -> new IllegalStateException("Provider role not configured"));
        if (!user.hasRole(RoleName.ROLE_PROVIDER)) {
            user.addRole(providerRole);
            userRepository.save(user);
        }
        return UserMapper.toResource(user);
    }
}
