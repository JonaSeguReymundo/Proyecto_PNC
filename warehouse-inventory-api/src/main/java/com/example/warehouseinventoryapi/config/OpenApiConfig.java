package com.example.warehouseinventoryapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configura Swagger con soporte para Bearer JWT.
 *
 * El "security" global hace que el botón Authorize aplique el token
 * a TODOS los endpoints. Login/refresh son públicos (permitAll), así que
 * mandarles el header de más no afecta.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Warehouse Inventory API",
                version     = "1.0",
                description = "Sistema de gestión de inventario N-Capas – Spring Boot + JWT",
                contact     = @Contact(name = "Equipo N-Capas")
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name         = "bearerAuth",
        type         = SecuritySchemeType.HTTP,
        scheme       = "bearer",
        bearerFormat = "JWT",
        description  = "Pega el access token obtenido en POST /api/auth/login"
)
public class OpenApiConfig {}
