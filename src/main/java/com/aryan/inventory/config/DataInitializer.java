package com.aryan.inventory.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.aryan.inventory.entity.Role;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;
    

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AdminProperties adminProperties) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(String... args) {

        if (!userRepository.existsByUsername(adminProperties.getUsername())) {

            User admin = new User();

            admin.setUsername(adminProperties.getUsername());
            admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("Default admin created.");
        }

    }

}