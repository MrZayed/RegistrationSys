package com.example.RegistrationSys.controller;

import com.example.RegistrationSys.dto.AuthRequest;
import com.example.RegistrationSys.dto.OtpRequest;
import com.example.RegistrationSys.dto.RegisterRequest;
import com.example.RegistrationSys.dto.RegisterResponse;
import com.example.RegistrationSys.entity.User;
import com.example.RegistrationSys.repository.UserRepository;
import com.example.RegistrationSys.security.JwtUtil;
import com.example.RegistrationSys.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private UserServiceImpl userService;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;
    @Autowired private UserRepository userRepo;




    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request).getBody();

        String otp = otpService.generateAndStoreOtp(request.getEmail());
        emailService.sendOtpEmail(request.getEmail(), otp);

        response.setMessage("Registration successful! OTP sent to " + request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest auth, BindingResult bindingResult) {
        return userService.login(auth, bindingResult);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String otp = req.get("otp");

        boolean isValid = otpService.validateOtp(email, otp);

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(true);
        userRepo.save(user);

        return ResponseEntity.ok("OTP verified, user activated successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshUserToken(@RequestBody Map<String, String> request) {
        return userService.refreshUserToken(request);
    }


}
