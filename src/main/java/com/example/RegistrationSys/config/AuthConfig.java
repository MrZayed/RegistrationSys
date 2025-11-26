package com.example.RegistrationSys.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "security")
public class AuthConfig {
    private boolean authentication;
    private Otp otp;
    private boolean rbac;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Otp {
        private boolean viaMail;
        private boolean viaSMS;
    }
}

