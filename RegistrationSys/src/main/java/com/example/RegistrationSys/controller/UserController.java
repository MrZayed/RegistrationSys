package com.example.RegistrationSys.controller;

import com.example.RegistrationSys.dto.AuthRequest;
import com.example.RegistrationSys.dto.RegisterRequest;
import com.example.RegistrationSys.dto.RegisterResponse;
import com.example.RegistrationSys.entity.User;
import com.example.RegistrationSys.security.JwtUtil;
import com.example.RegistrationSys.service.CustomUserDetailsService;
import com.example.RegistrationSys.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class UserController {

    @Autowired private UserService userService;

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User u) {
        return userService.updateUser(id, u);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<User> get(@PathVariable String id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userService.getAllUsers();
    }

}

