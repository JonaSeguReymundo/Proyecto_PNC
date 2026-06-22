package com.example.warehouseinventoryapi.config;

import com.example.warehouseinventoryapi.entity.Role;
import com.example.warehouseinventoryapi.entity.RoleName;
import com.example.warehouseinventoryapi.entity.User;
import com.example.warehouseinventoryapi.repository.RoleRepository;
import com.example.warehouseinventoryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

/**
 * Inicializa roles y un usuario ADMINISTRADOR por defecto al arrancar.
 * Solo inserta si aún no existen (idempotente).
 *
 * Credenciales por defecto:
 *   username: admin
 *   password: Admin1234!
 * ⚠ Cambiar en producción vía variable de entorno ADMIN_PASSWORD.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository  roleRepository;
    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        Arrays.stream(RoleName.values()).forEach(name -> {
            if (roleRepository.findByName(name).isEmpty()) {
                roleRepository.save(Role.builder().name(name).build());
                log.info("Role created: {}", name);
            }
        });
    }

    private void seedAdminUser() {
        if (userRepository.existsByUsername("admin")) return;

        Role adminRole = roleRepository.findByName(RoleName.ADMINISTRADOR)
                .orElseThrow(() -> new IllegalStateException("ADMINISTRADOR role not found"));

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Admin1234!"))
                .fullName("System Administrator")
                .roles(Set.of(adminRole))
                .active(true)
                .build();

        userRepository.save(admin);
        log.info("Default admin user created (username: admin)");
    }
}
