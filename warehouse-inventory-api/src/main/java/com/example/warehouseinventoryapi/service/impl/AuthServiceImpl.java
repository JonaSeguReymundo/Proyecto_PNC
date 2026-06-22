package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.LoginRequest;
import com.example.warehouseinventoryapi.dto.request.RefreshTokenRequest;
import com.example.warehouseinventoryapi.dto.response.AuthResponse;
import com.example.warehouseinventoryapi.entity.User;
import com.example.warehouseinventoryapi.repository.UserRepository;
import com.example.warehouseinventoryapi.security.util.JwtProperties;
import com.example.warehouseinventoryapi.security.util.JwtUtil;
import com.example.warehouseinventoryapi.service.AuditLogService;
import com.example.warehouseinventoryapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Lógica de autenticación y rotación de tokens.
 *
 * Flujo de login:
 *   1. AuthenticationManager valida usuario/contraseña contra la BD.
 *   2. Se generan access + refresh token.
 *   3. Se registra en audit_log.
 *
 * Flujo de refresh (stateless con rotación):
 *   1. Verificar que el token recibido sea un refresh token válido.
 *   2. Emitir un NUEVO par de tokens (el anterior queda implícitamente invalidado
 *      por el cliente al descartar el refresh token anterior).
 *   3. Registrar en audit_log.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository        userRepository;
    private final JwtUtil               jwtUtil;
    private final JwtProperties         jwtProps;
    private final AuditLogService       auditLogService;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user  = (User) auth.getPrincipal();
        String roles = buildRolesClaim(auth);

        String accessToken  = jwtUtil.generateAccessToken(user.getUsername(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), roles);

        auditLogService.record(user.getUsername(), "LOGIN", "users",
                "Login exitoso desde el sistema");

        return new AuthResponse(
                accessToken, refreshToken,
                jwtProps.getAccessTokenExpirationMs(),
                jwtProps.getRefreshTokenExpirationMs()
        );
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        String token = request.refreshToken();

        // Lanzará JwtException si está expirado o es inválido → GlobalExceptionHandler lo captura
        if (!jwtUtil.isRefreshToken(token)) {
            throw new IllegalArgumentException("The provided token is not a refresh token");
        }

        String username = jwtUtil.extractUsername(token);

        // Verificar que el usuario aún exista y esté activo
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }

        // Obtener roles actualizados desde la BD en lugar de confiar en el token viejo
        String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Rotación: emitir par nuevo
        String newAccess  = jwtUtil.generateAccessToken(username, roles);
        String newRefresh = jwtUtil.generateRefreshToken(username, roles);

        auditLogService.record(username, "REFRESH", "users",
                "Refresh token rotado");

        return new AuthResponse(
                newAccess, newRefresh,
                jwtProps.getAccessTokenExpirationMs(),
                jwtProps.getRefreshTokenExpirationMs()
        );
    }

    /** Construye el string de roles para el claim JWT: "ROLE_ADMINISTRADOR,ROLE_OPERARIO" */
    private String buildRolesClaim(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
