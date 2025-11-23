package com.example.RegistrationSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName ;
    private String email ;
    private String role ;
    private Boolean isActive ;
}
