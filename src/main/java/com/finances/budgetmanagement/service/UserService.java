package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.AuthRequest;
import com.finances.budgetmanagement.dto.AuthResponse;

public interface UserService {

    AuthResponse authenticateUser(AuthRequest authRequest);

    String registerUser(AuthRequest authRequest);
}
