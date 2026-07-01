package com.example.warehouseinventoryapi.dto.response;

/**
 * Respuesta devuelta en login y refresh.
 * Contiene el access token (corta duración) y el refresh token (larga duración).
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long accessExpiresIn,
        long refreshExpiresIn
) {
    public AuthResponse(String accessToken, String refreshToken,
                        long accessExpiresIn, long refreshExpiresIn) {
        this(accessToken, refreshToken, "Bearer", accessExpiresIn, refreshExpiresIn);
    }
}
