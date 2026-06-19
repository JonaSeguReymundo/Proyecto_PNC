package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.LoginRequest;
import com.example.warehouseinventoryapi.dto.request.RefreshTokenRequest;
import com.example.warehouseinventoryapi.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
}
