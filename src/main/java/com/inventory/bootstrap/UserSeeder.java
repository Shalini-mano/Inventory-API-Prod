package com.inventory.bootstrap;
import com.inventory.model.Role;
import com.inventory.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.inventory.repo.UserRepository;

import java.time.LocalDateTime;

@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        User admin = new User(
                "admin",                            // username
                "admin@example.com",                // email
                passwordEncoder.encode("admin123"), // passwordHash
                Role.ADMIN                          // role
        );

        User user = new User(
                "user",
                "user@example.com",
                passwordEncoder.encode("user123"),
                Role.USER
        );

        userRepository.save(admin);
        userRepository.save(user);

        System.out.println("Seeded database with Admin and User accounts.");
    }
}
