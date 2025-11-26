package com.example.RegistrationSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthConfigResponse {
    private boolean authentication;
    private boolean otpViaMail;
    private boolean otpViaSMS;
    private boolean rbac;
}

