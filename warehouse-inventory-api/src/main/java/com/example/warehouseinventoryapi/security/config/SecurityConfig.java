package com.example.warehouseinventoryapi.security.config;

import com.example.warehouseinventoryapi.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración principal de Spring Security.
 *
 * Reglas de acceso por rol (según Proyecto 8):
 *   - ADMINISTRADOR : acceso total (configura almacenes/políticas, ve auditoría global)
 *   - JEFE_ALMACEN  : gestiona inventario, ordena traslados, revisa alertas
 *   - OPERARIO      : registra entradas/salidas y escanea códigos; lectura en el resto
 *
 * IMPORTANTE: las reglas se evalúan EN ORDEN. Las más específicas (que incluyen
 * al OPERARIO en entradas/salidas) van ANTES que las reglas generales por método.
 *
 * Swagger (/swagger-ui/**, /api-docs/**) es público para facilitar la evaluación.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity           // habilita @PreAuthorize en controllers/services
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService       userDetailsService;

    // ── Rutas públicas ────────────────────────────────────────────────────────
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/refresh",
            // Swagger
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/audit/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/inventory/entries/**")
                        .hasAnyRole("ADMINISTRADOR", "JEFE_ALMACEN", "OPERARIO")
                        .requestMatchers(HttpMethod.POST, "/api/inventory/exits/**")
                        .hasAnyRole("ADMINISTRADOR", "JEFE_ALMACEN", "OPERARIO")
                        .requestMatchers("/api/scan/**")
                        .hasAnyRole("ADMINISTRADOR", "JEFE_ALMACEN", "OPERARIO")
                        .requestMatchers(HttpMethod.POST,   "/api/**").hasAnyRole("ADMINISTRADOR", "JEFE_ALMACEN")
                        .requestMatchers(HttpMethod.PUT,    "/api/**").hasAnyRole("ADMINISTRADOR", "JEFE_ALMACEN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/**").hasAnyRole("ADMINISTRADOR", "JEFE_ALMACEN")

                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMINISTRADOR")

                        // lectura: cualquier usuario autenticado
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}