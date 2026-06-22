# Integrante 2 – Seguridad y API REST

## Stack
- Spring Boot 4.1 · Java 21 · Spring Security 6
- OAuth2 Resource Server (HMAC-SHA256 / HS256)
- SpringDoc OpenAPI 2.x (Swagger UI protegido con JWT)
- AOP para auditoría automática

---

## Archivos entregados

```
pom.xml                          ← dependencias actualizadas
application.yaml                 ← configuración JWT + Swagger
schema_security.sql              ← DER y datos semilla

entity/
  RoleName.java                  ← enum: ADMINISTRADOR | JEFE_ALMACEN | OPERARIO
  Role.java                      ← entidad JPA tabla roles
  User.java                      ← entidad JPA tabla users (implementa UserDetails)
  AuditLog.java                  ← entidad JPA tabla audit_log

repository/
  UserRepository.java
  RoleRepository.java
  AuditLogRepository.java

security/
  util/
    JwtProperties.java           ← mapea security.jwt.* del yaml
    JwtUtil.java                 ← genera/decodifica tokens (access + refresh)
  config/
    JwtConfig.java               ← beans JwtEncoder / JwtDecoder (NimbusJwt)
    SecurityConfig.java          ← filtros, roles, endpoints públicos
  filter/
    JwtAuthenticationFilter.java ← extrae Bearer token y autentica cada request
  service/
    UserDetailsServiceImpl.java  ← carga usuario desde BD para Spring Security

service/
  AuthService.java / impl/AuthServiceImpl.java      ← login + refresh
  UserService.java / impl/UserServiceImpl.java       ← CRUD usuarios
  AuditLogService.java / impl/AuditLogServiceImpl.java

controller/
  AuthController.java            ← POST /api/auth/login | /api/auth/refresh
  UserController.java            ← CRUD /api/users/** (solo ADMINISTRADOR)
  AuditLogController.java        ← GET /api/audit/** (ADMINISTRADOR | JEFE_ALMACEN)

dto/request/  LoginRequest · RefreshTokenRequest · CreateUserRequest · UpdateUserRequest
dto/response/ AuthResponse · UserResponse · AuditLogResponse

audit/
  AuditAspect.java               ← AOP: registra create/update/delete automáticamente

config/
  OpenApiConfig.java             ← Swagger con botón Authorize (Bearer JWT)
  DataInitializer.java           ← crea roles y usuario admin al arrancar

exception/
  GlobalExceptionHandler.java    ← extiende el del Int.1 con 401/403/JWT errors
```

---

## Variables de entorno requeridas

| Variable | Ejemplo | Descripción |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://host:5432/warehouse` | URL de PostgreSQL |
| `DB_USER` | `postgres` | Usuario BD |
| `DB_PASSWORD` | `secret` | Contraseña BD |
| `JWT_SECRET` | `(base64 64 chars)` | Clave HMAC para firmar tokens |
| `JWT_ACCESS_EXP_MS` | `3600000` | Expiración access token (ms) – default 1h |
| `JWT_REFRESH_EXP_MS` | `604800000` | Expiración refresh token (ms) – default 7d |

Generar JWT_SECRET seguro:
```bash
openssl rand -base64 64
```

---

## Flujo de autenticación

```
POST /api/auth/login
Body: { "username": "admin", "password": "Admin1234!" }

→ 200 OK
{
  "accessToken":  "eyJ...",   // usar en Authorization: Bearer <token>
  "refreshToken": "eyJ...",   // guardar; usar en /api/auth/refresh
  "tokenType":    "Bearer",
  "accessExpiresIn":  3600000,
  "refreshExpiresIn": 604800000
}
```

**Rotación de refresh token (stateless):**
```
POST /api/auth/refresh
Body: { "refreshToken": "eyJ..." }

→ 200 OK  (nuevo par de tokens; el anterior debe descartarse)
```

---

## Roles y permisos

| Endpoint | ADMINISTRADOR | JEFE_ALMACEN | OPERARIO |
|---|:---:|:---:|:---:|
| POST/PUT/PATCH `/api/**` | ✅ | ✅ | ❌ |
| DELETE `/api/**` | ✅ | ❌ | ❌ |
| GET `/api/**` | ✅ | ✅ | ✅ |
| `/api/users/**` | ✅ | ❌ | ❌ |
| `/api/audit/**` | ✅ | ✅ | ❌ |
| `/api/auth/**` | público | público | público |

---

## Swagger UI

```
https://backend-url/swagger-ui.html
```

1. Ir a `POST /api/auth/login` → Execute
2. Copiar `accessToken` de la respuesta
3. Clic en **Authorize** → pegar el token
4. Todos los endpoints protegidos ya envían el header automáticamente

---

## Usuario admin por defecto

```
username : admin
password : Admin1234!
```

> ⚠ Cambiar en producción. El `DataInitializer` solo crea el usuario si no existe.

---

## Auditoría automática (AOP)

El `AuditAspect` intercepta todos los métodos `create`, `update` y `delete`
de los servicios y los registra en `audit_log` sin modificar ningún código de negocio.
El login y el refresh también se registran manualmente desde `AuthServiceImpl`.

Consultar vía:
```
GET /api/audit                      → todos los registros
GET /api/audit/user/{username}      → por usuario
GET /api/audit/table/{table}        → por tabla (products, warehouses, etc.)
```

---

## HTTP Status codes implementados

| Código | Cuándo |
|---|---|
| 200 | GET, login, refresh |
| 201 | POST de creación |
| 204 | DELETE |
| 400 | Validación fallida, argumento inválido |
| 401 | Token inválido/expirado, credenciales incorrectas |
| 403 | Rol insuficiente |
| 404 | Recurso no encontrado |
| 500 | Error inesperado del servidor |
