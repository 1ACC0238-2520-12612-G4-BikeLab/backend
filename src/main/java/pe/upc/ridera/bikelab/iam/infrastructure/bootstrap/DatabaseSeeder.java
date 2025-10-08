package pe.upc.ridera.bikelab.iam.infrastructure.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pe.upc.ridera.bikelab.iam.domain.model.aggregates.User;
import pe.upc.ridera.bikelab.iam.domain.model.valueobjects.RoleName;
import pe.upc.ridera.bikelab.iam.domain.persistence.RoleRepository;
import pe.upc.ridera.bikelab.iam.domain.persistence.UserRepository;

/**
 * Este inicializador crea datos mínimos para el arranque del contexto IAM.
 */ 
@Component
@Transactional
public class DatabaseSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            User admin = new User("admin@bikelab.io",
                    passwordEncoder.encode("Admin#123"),
                    "Admin",
                    "User");
            roleRepository.findByName(RoleName.ROLE_ADMIN).ifPresent(admin::addRole);
            userRepository.save(admin);
        }
    }
}
