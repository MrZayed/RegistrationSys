package com.example.RegistrationSys.service;

import com.example.RegistrationSys.config.AuthConfig;
import com.example.RegistrationSys.dto.AuthRequest;
import com.example.RegistrationSys.dto.RegisterRequest;
import com.example.RegistrationSys.dto.RegisterResponse;
import com.example.RegistrationSys.entity.Role;
import com.example.RegistrationSys.entity.User;
import com.example.RegistrationSys.mapper.UserMapper;
import com.example.RegistrationSys.repository.RoleRepository;
import com.example.RegistrationSys.repository.UserRepository;
import com.example.RegistrationSys.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthConfig authConfig ;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("This user already exists");
        }
        if (!request.getPassword().equals(request.getConfirmPass())) {
            throw new RuntimeException("Passwords do not match");
        }
        // from properties, you can edit
        if (!authConfig.getOtp().isViaSMS()) {
            throw new RuntimeException("Email OTP is disabled");
        }

        Role role = roleRepo.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setIsActive(false); // must be verified before login
        userRepo.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setMessage("Registration successful! Please verify your email first.");
        response.setUserName(UserMapper.toDto(user));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> login(AuthRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error -> error.getField() + ": " + error.getDefaultMessage())
                            .toList()
            );
        }

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getIsActive()) {
            throw new RuntimeException("User is not active. Verify your email first.");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword())
            );

            String accessToken = jwtUtil.generateToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    @Override
    public ResponseEntity<?> refreshUserToken(Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            String username = jwtUtil.extractUsername(refreshToken);

            User user = userRepo.findByEmail(username)
                    .or(() -> userRepo.findByName(username))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!jwtUtil.isTokenValid(refreshToken, new org.springframework.security.core.userdetails.User(
                    username, user.getPassword(), new ArrayList<>()
            ))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }

            String newAccessToken = jwtUtil.generateToken(username);

            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }


    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User updateUser(String email, User updatedUser) {
        User user = userRepo.findByEmail(email) .orElseThrow(() -> new RuntimeException("User not found"));
        // update only allowed fields
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());

        // if updating password
        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public ResponseEntity<?> deleteUser(String email) {
        return null;
    }


}
