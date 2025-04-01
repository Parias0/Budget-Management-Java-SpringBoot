package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.auth.AuthRequest;
import com.finances.budgetmanagement.dto.auth.AuthResponse;
import com.finances.budgetmanagement.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint do logowania
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        AuthResponse authResponse = userService.authenticateUser(authRequest, response);

        if ("Authentication failed".equals(authResponse.getMessage())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody AuthRequest authRequest) {
        String response = userService.registerUser(authRequest);

        if (response.equals("Username cannot be empty") || response.equals("Password cannot be empty") || response.equals("Username is already taken")) {
            return ResponseEntity.badRequest().body(Map.of("message", response));
        }

        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletResponse response) {
        String message = userService.logoutUser(response);
        return ResponseEntity.ok(message);
    }
}
