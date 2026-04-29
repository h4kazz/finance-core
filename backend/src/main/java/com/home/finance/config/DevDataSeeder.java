package com.home.finance.config;

import com.home.finance.user.User;
import com.home.finance.user.UserRepository;
import com.home.finance.user.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DevDataSeeder {

    @Bean
    CommandLineRunner seedDevUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, "user", "user@user.com", "password", Set.of(UserRole.USER));
            createUserIfMissing(userRepository, passwordEncoder, "admin", "admin@admin.com", "admin123", Set.of(UserRole.USER, UserRole.ADMIN));
        };
    }

    private static void createUserIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String name,
            String email,
            String password,
            Set<UserRole> roles
    ) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        userRepository.save(new User(name, email, passwordEncoder.encode(password), roles));
    }
}
