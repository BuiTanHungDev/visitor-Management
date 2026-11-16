package com.visitor.management.config;

import com.visitor.management.user.entity.Role;
import com.visitor.management.user.entity.User;
import com.visitor.management.user.entity.UserRole;
import com.visitor.management.user.entity.UserRoleId;
import com.visitor.management.user.repository.RoleRepository;
import com.visitor.management.user.repository.UserRepository;
import com.visitor.management.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.password:}")
    private String initialPassword;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        if (initialPassword == null || initialPassword.isBlank()) {
            log.info("Admin initialization skipped: no initial password configured.");
            return;
        }

        if (userRepository.existsByUsernameIgnoreCase("admin")) {
            log.info("Admin account already exists. Skipping initialization.");
            return;
        }

        Role adminRole = roleRepository.findByCodeIgnoreCase("ADMIN")
                .orElseThrow(() -> new IllegalStateException(
                        "Role ADMIN does not exist in database."
                ));

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode(initialPassword));
        admin.setFullName("Quản trị viên hệ thống");
        admin.setEmail("admin@visitor.local");
        admin.setStatus("ACTIVE");

        admin = userRepository.save(admin);

        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(admin.getId());
        userRoleId.setRoleId(adminRole.getId());

        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        userRole.setUser(admin);
        userRole.setRole(adminRole);

        userRoleRepository.save(userRole);

        log.info("Initial admin account created successfully.");
    }
}