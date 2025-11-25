package com.example.RegistrationSys.controller;

import com.example.RegistrationSys.entity.User;
import com.example.RegistrationSys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User u) {
        return userService.updateUser(id, u);
    }

    @GetMapping("/{id}")
    public Optional<User> get(@PathVariable String id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/")
    public List<User> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Controller sees: " + auth);
        return userService.getAllUsers();
    }
    @GetMapping()
    public String getAlll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Controller sees: " + auth);
        return "";
    }
}
