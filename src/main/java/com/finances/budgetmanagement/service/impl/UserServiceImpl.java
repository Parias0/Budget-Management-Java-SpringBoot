package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.AuthRequest;
import com.finances.budgetmanagement.dto.AuthResponse;
import com.finances.budgetmanagement.entity.Account;
import com.finances.budgetmanagement.entity.Role;
import com.finances.budgetmanagement.entity.User;
import com.finances.budgetmanagement.enums.RoleName;
import com.finances.budgetmanagement.repository.RoleRepository;
import com.finances.budgetmanagement.repository.UserRepository;
import com.finances.budgetmanagement.security.JwtTokenUtil;
import com.finances.budgetmanagement.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse authenticateUser(AuthRequest authRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            String token = jwtTokenUtil.generateToken(authentication.getName());

            // Logowanie udanego logowania
            System.out.println("User authenticated successfully: " + authRequest.getUsername());

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // Ustaw na true, jeśli korzystasz z HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 24);

            response.addCookie(jwtCookie);
            return new AuthResponse("User logged in successfully");
        } catch (Exception e) {
            // Logowanie błędu
            System.out.println("Authentication failed: " + e.getMessage());
            return new AuthResponse("Authentication failed");
        }
    }


    @Override
    public String registerUser(AuthRequest authRequest) {
        if (userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            return "Username is already taken";
        }

        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ROLE_USER);
                    return roleRepository.save(role);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        Account account = new Account();
        account.setUser(user);
        user.getAccounts().add(account);

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public String logoutUser(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        response.addCookie(jwtCookie);

        return "User logged out successfully";
    }
}
