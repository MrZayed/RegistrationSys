package com.example.RegistrationSys;

import com.example.RegistrationSys.entity.Role;
import com.example.RegistrationSys.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RegistrationSysApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationSysApplication.class, args);
	}
}
