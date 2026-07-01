package com.example.warehouseinventoryapi.security.config;

import com.example.warehouseinventoryapi.security.util.JwtProperties;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Configura el JwtEncoder y JwtDecoder usando HMAC-SHA256.
 * Spring Security OAuth2 Resource Server usa NimbusJwt internamente.
 */
@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties props;

    private SecretKeySpec secretKeySpec() {
        byte[] keyBytes = props.getSecret().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKeySpec()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKeySpec()).build();
    }
}
