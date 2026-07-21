package com.tallermecanico.api.service;

import com.tallermecanico.api.dto.request.LoginRequest;
import com.tallermecanico.api.dto.request.RegisterRequest;
import com.tallermecanico.api.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
