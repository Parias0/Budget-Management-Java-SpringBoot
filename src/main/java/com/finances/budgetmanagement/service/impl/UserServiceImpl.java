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
    public AuthResponse authenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        String token = jwtTokenUtil.generateToken(authentication.getName());
        return new AuthResponse(token);
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

        // Tworzenie konta z zerowym balansem
        Account account = new Account();
        account.setUser(user);
        user.getAccounts().add(account);

        userRepository.save(user);

        return "User registered successfully";
    }
}
