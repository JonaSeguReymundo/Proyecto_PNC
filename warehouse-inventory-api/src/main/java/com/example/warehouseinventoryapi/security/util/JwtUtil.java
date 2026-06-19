package com.example.warehouseinventoryapi.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Genera y decodifica JWTs usando Spring Security OAuth2 Resource Server
 * con clave HMAC-SHA256 (algoritmo HS256).
 *
 * Tokens:
 *  - access  → claim "type": "access",  corta duración
 *  - refresh → claim "type": "refresh", larga duración
 *
 * Rotación stateless: al usar el refresh token se emite un nuevo par
 * (access + refresh) sin persistir nada en BD.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtEncoder    jwtEncoder;
    private final JwtDecoder    jwtDecoder;
    private final JwtProperties props;

    // ── Generación ────────────────────────────────────────────────────────────

    public String generateAccessToken(String username, String roles) {
        return buildToken(username, roles, "access", props.getAccessTokenExpirationMs());
    }

    public String generateRefreshToken(String username, String roles) {
        return buildToken(username, roles, "refresh", props.getRefreshTokenExpirationMs());
    }

    private String buildToken(String username, String roles, String type, long expirationMs) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("warehouse-api")
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plus(expirationMs, ChronoUnit.MILLIS))
                .claim("roles", roles)
                .claim("type", type)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    // ── Decodificación ────────────────────────────────────────────────────────

    public Jwt decode(String token) {
        return jwtDecoder.decode(token);
    }

    public String extractUsername(String token) {
        return decode(token).getSubject();
    }

    public String extractType(String token) {
        return decode(token).getClaimAsString("type");
    }

    public String extractRoles(String token) {
        return decode(token).getClaimAsString("roles");
    }

    public boolean isAccessToken(String token) {
        return "access".equals(extractType(token));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractType(token));
    }
}
