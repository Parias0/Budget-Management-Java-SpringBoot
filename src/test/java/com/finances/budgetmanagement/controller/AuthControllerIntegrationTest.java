package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.auth.AuthRequest;
import com.finances.budgetmanagement.entity.Role;
import com.finances.budgetmanagement.entity.User;
import com.finances.budgetmanagement.enums.RoleName;
import com.finances.budgetmanagement.repository.RoleRepository;
import com.finances.budgetmanagement.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper; // do mapowania obiektów na JSON

    @BeforeEach
    public void setUp() {
        // Opcjonalnie wyczyść bazę, aby testy były idempotentne
        userRepository.deleteAll();
        // Możesz także ustawić domyślną rolę, jeśli nie została utworzona
        if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
            Role role = new Role();
            role.setName(RoleName.ROLE_USER);
            roleRepository.save(role);
        }
    }

    // Test rejestracji użytkownika
    @Test
    public void whenRegisterUser_thenReturnSuccessMessage() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("testpassword");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully")));
    }

    // Test logowania użytkownika
    @Test
    public void whenLoginUser_thenReturnJwtToken() throws Exception {
        // Najpierw zarejestruj użytkownika bezpośrednio w repozytorium
        User user = new User();
        user.setUsername("loginuser");
        user.setPassword(passwordEncoder.encode("loginpassword"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).get());
        user.setRoles(roles);
        userRepository.save(user);

        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("loginuser");
        loginRequest.setPassword("loginpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                // Sprawdzamy, czy odpowiedź zawiera token (np. pole "token")
                .andExpect(content().string(containsString("token")));
    }
}
