package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.LoginRequest;
import com.example.warehouseinventoryapi.dto.request.RefreshTokenRequest;
import com.example.warehouseinventoryapi.dto.response.AuthResponse;
import com.example.warehouseinventoryapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login y gestión de tokens JWT")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Devuelve access token + refresh token.
     * HTTP 200 OK (no 201 porque no se crea un recurso, se inicia una sesión).
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Devuelve access token y refresh token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * POST /api/auth/refresh
     * Recibe el refresh token y emite un nuevo par (rotación stateless).
     */
    @PostMapping("/refresh")
    @Operation(summary = "Rotar refresh token", description = "Emite un nuevo par de tokens")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
