package com.example.RegistrationSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class OtpRequest {
    String otp ;
    String email;
}
