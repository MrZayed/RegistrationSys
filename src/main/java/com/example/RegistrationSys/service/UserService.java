package com.example.RegistrationSys.service;

import com.example.RegistrationSys.dto.AuthRequest;
import com.example.RegistrationSys.dto.RegisterRequest;
import com.example.RegistrationSys.dto.RegisterResponse;
import com.example.RegistrationSys.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    ResponseEntity<RegisterResponse> register (RegisterRequest request);
    ResponseEntity<?> login(AuthRequest request, BindingResult bindingResult);
    ResponseEntity<?> refreshUserToken (Map<String, String> request);
    List<User> getAllUsers();
    User updateUser(String email, User updatedUser);
    Optional<User> getUser(String email);
    ResponseEntity<?> deleteUser(String email);
}
