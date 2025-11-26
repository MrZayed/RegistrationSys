package com.example.RegistrationSys.dto;


import lombok.Data;

@Data
public class AuthConfigUpdateRequest {
    private Boolean authentication;
    private Boolean otpViaMail;
    private Boolean otpViaSMS;
    private Boolean rbac;
}
