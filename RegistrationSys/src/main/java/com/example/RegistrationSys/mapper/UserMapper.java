package com.example.RegistrationSys.mapper;

import com.example.RegistrationSys.dto.UserDto;
import com.example.RegistrationSys.entity.User;

public class UserMapper {

    public static UserDto  toDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getIsActive()
        );
    }
}

