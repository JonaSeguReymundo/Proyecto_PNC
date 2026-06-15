package com.example.warehouseinventoryapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "racks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aisle_id", nullable = false)
    private Aisle aisle;

    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Level> levels;
}