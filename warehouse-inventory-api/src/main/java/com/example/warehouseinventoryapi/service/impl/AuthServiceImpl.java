package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.LoginRequest;
import com.example.warehouseinventoryapi.dto.request.RefreshTokenRequest;
import com.example.warehouseinventoryapi.dto.response.AuthResponse;
import com.example.warehouseinventoryapi.entity.User;
import com.example.warehouseinventoryapi.exception.BadRequestException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.repository.UserRepository;
import com.example.warehouseinventoryapi.security.util.JwtProperties;
import com.example.warehouseinventoryapi.security.util.JwtUtil;
import com.example.warehouseinventoryapi.service.AuditLogService;
import com.example.warehouseinventoryapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository        userRepository;
    private final JwtUtil                jwtUtil;
    private final JwtProperties          jwtProps;
    private final AuditLogService        auditLogService;

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request.username() == null || request.username().isBlank()) {
            throw new BadRequestException("Username is required to log in.");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new BadRequestException("Password is required to log in.");
        }

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user  = (User) auth.getPrincipal();
        String roles = buildRolesClaim(auth);

        String accessToken  = jwtUtil.generateAccessToken(user.getUsername(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), roles);

        auditLogService.record(user.getUsername(), "LOGIN", "users",
                "Successful login from the system");

        return new AuthResponse(
                accessToken, refreshToken,
                jwtProps.getAccessTokenExpirationMs(),
                jwtProps.getRefreshTokenExpirationMs()
        );
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        String token = request.refreshToken();

        if (token == null || token.isBlank()) {
            throw new BadRequestException("Refresh token is required.");
        }

        if (!jwtUtil.isRefreshToken(token)) {
            throw new BadRequestException("The provided token is not a valid refresh token.");
        }

        String username = jwtUtil.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the system: " + username));

        if (!user.isEnabled()) {
            throw new BadRequestException("The user account '" + username + "' is currently disabled.");
        }

        String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String newAccess  = jwtUtil.generateAccessToken(username, roles);
        String newRefresh = jwtUtil.generateRefreshToken(username, roles);

        auditLogService.record(username, "REFRESH", "users",
                "Refresh token successfully rotated");

        return new AuthResponse(
                newAccess, newRefresh,
                jwtProps.getAccessTokenExpirationMs(),
                jwtProps.getRefreshTokenExpirationMs()
        );
    }

    private String buildRolesClaim(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}