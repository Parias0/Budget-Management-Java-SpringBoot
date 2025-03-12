package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.AuthRequest;
import com.finances.budgetmanagement.dto.AuthResponse;
import com.finances.budgetmanagement.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint do logowania
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        AuthResponse authResponse = userService.authenticateUser(authRequest, response);

        if ("Authentication failed".equals(authResponse.getToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        return ResponseEntity.ok(authResponse);
    }


    // Endpoint do rejestracji użytkownika
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest authRequest) {
        String response = userService.registerUser(authRequest);
        if (response.equals("Username is already taken")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletResponse response) {
        String message = userService.logoutUser(response);
        return ResponseEntity.ok(message);
    }
}
