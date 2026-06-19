package com.example.warehouseinventoryapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configura Swagger con soporte para Bearer JWT.
 *
 * En la UI aparece el botón "Authorize" donde se pega el access token.
 * Todos los endpoints marcados con @SecurityRequirement(name = "bearerAuth")
 * enviarán el header automáticamente.
 *
 * URL: https://backend-url/swagger-ui.html
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Warehouse Inventory API",
                version     = "1.0",
                description = "Sistema de gestión de inventario N-Capas – Spring Boot 4 + JWT",
                contact     = @Contact(name = "Equipo N-Capas")
        )
)
@SecurityScheme(
        name        = "bearerAuth",
        type        = SecuritySchemeType.HTTP,
        scheme      = "bearer",
        bearerFormat = "JWT",
        description = "Pega el access token obtenido en POST /api/auth/login"
)
public class OpenApiConfig {}
