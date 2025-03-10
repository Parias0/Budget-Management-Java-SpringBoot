package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.AuthRequest;
import com.finances.budgetmanagement.dto.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    AuthResponse authenticateUser(AuthRequest authRequest, HttpServletResponse response);

    String registerUser(AuthRequest authRequest);

    String logoutUser(HttpServletResponse response);
}
