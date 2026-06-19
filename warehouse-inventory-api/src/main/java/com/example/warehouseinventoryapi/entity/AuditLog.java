package com.example.warehouseinventoryapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Tabla audit_log – registra quién hizo qué y cuándo.
 * Campos según el proyecto: id, usuario, accion, tabla, fecha + detalle opcional.
 */
@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que ejecutó la acción (username). */
    @Column(nullable = false, length = 60)
    private String username;

    /** Tipo de acción: CREATE, UPDATE, DELETE, LOGIN, LOGOUT, REFRESH. */
    @Column(nullable = false, length = 30)
    private String action;

    /** Tabla / entidad afectada: users, products, warehouses, etc. */
    @Column(name = "affected_table", nullable = false, length = 50)
    private String affectedTable;

    /** Detalle adicional (payload resumido, ID del recurso, etc.). */
    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
