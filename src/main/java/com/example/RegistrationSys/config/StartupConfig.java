package com.example.RegistrationSys.config;

import com.example.RegistrationSys.entity.Role;
import com.example.RegistrationSys.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepo) {
        return args -> {
            if (roleRepo.findByName("USER").isEmpty()) {
                roleRepo.save(new Role(null, "USER"));
            }
            if (roleRepo.findByName("ADMIN").isEmpty()) {
                roleRepo.save(new Role(null, "ADMIN"));
            }
        };
    }
}

