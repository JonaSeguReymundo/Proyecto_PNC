package com.example.warehouseinventoryapi.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Role – cada rol es una fila en la tabla roles.
 * Se relaciona con User a través de la tabla intermedia user_roles.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleName name;
}
