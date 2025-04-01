package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.auth.AuthRequest;
import com.finances.budgetmanagement.dto.auth.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    AuthResponse authenticateUser(AuthRequest authRequest, HttpServletResponse response);

    String registerUser(AuthRequest authRequest);

    String logoutUser(HttpServletResponse response);

}
