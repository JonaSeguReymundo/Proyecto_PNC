package com.example.warehouseinventoryapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "aisles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aisle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "aisle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rack> racks;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean active = true;
}