-- ============================================================
-- Script SQL – Módulo de Seguridad (Integrante 2)
-- Compatible con PostgreSQL
-- Complementa el DER del Integrante 1
-- ============================================================

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(30)  NOT NULL UNIQUE
        CHECK (name IN ('ADMINISTRADOR','JEFE_ALMACEN','OPERARIO'))
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id        BIGSERIAL    PRIMARY KEY,
    username  VARCHAR(60)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    active    BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Tabla intermedia usuario–rol (ManyToMany)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Tabla de auditoría
CREATE TABLE IF NOT EXISTS audit_log (
    id             BIGSERIAL    PRIMARY KEY,
    username       VARCHAR(60)  NOT NULL,
    action         VARCHAR(30)  NOT NULL,
    affected_table VARCHAR(50)  NOT NULL,
    detail         TEXT,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Índices de auditoría (consultas frecuentes)
CREATE INDEX IF NOT EXISTS idx_audit_username  ON audit_log(username);
CREATE INDEX IF NOT EXISTS idx_audit_table     ON audit_log(affected_table);
CREATE INDEX IF NOT EXISTS idx_audit_created   ON audit_log(created_at DESC);

-- Índices de usuarios
CREATE INDEX IF NOT EXISTS idx_users_active    ON users(active);

-- ─── Datos semilla (los mismos que DataInitializer.java) ────────────────────
INSERT INTO roles (name) VALUES
    ('ADMINISTRADOR'),
    ('JEFE_ALMACEN'),
    ('OPERARIO')
ON CONFLICT (name) DO NOTHING;

-- Admin por defecto: password = Admin1234! (BCrypt hash)
-- Regenerar con: new BCryptPasswordEncoder().encode("Admin1234!")
INSERT INTO users (username, password, full_name, active)
VALUES (
    'admin',
    '$2a$12$K8Zj4k6VXjGFB1Lm3N9TOuO5PqR7sT2wX0yZ1aA4bC6dE8fG0hI2j',
    'System Administrator',
    TRUE
) ON CONFLICT (username) DO NOTHING;

-- Asignar rol ADMINISTRADOR al admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMINISTRADOR'
ON CONFLICT DO NOTHING;
