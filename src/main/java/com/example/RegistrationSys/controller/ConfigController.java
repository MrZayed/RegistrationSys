package com.example.RegistrationSys.controller;

import com.example.RegistrationSys.config.AuthConfig;
import com.example.RegistrationSys.dto.AuthConfigResponse;
import com.example.RegistrationSys.dto.AuthConfigUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    @Autowired
    private AuthConfig authConfig;

    @GetMapping
    public AuthConfigResponse getConfig() {
        return new AuthConfigResponse(
                authConfig.isAuthentication(),
                authConfig.getOtp().isViaMail(),
                authConfig.getOtp().isViaSMS(),
                authConfig.isRbac()
        );
    }


    @PatchMapping
    public AuthConfigResponse updateConfig(@RequestBody AuthConfigUpdateRequest request) {

        if (request.getAuthentication() != null)
            authConfig.setAuthentication(request.getAuthentication());

        if (request.getOtpViaMail() != null)
            authConfig.getOtp().setViaMail(request.getOtpViaMail());

        if (request.getOtpViaSMS() != null)
            authConfig.getOtp().setViaSMS(request.getOtpViaSMS());

        if (request.getRbac() != null)
            authConfig.setRbac(request.getRbac());

        return new AuthConfigResponse(
                authConfig.isAuthentication(),
                authConfig.getOtp().isViaMail(),
                authConfig.getOtp().isViaSMS(),
                authConfig.isRbac()
        );
    }
}
