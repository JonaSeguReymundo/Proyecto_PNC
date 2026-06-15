package com.example.warehouseinventoryapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    private Double weight;
    private Double length;
    private Double width;
    private Double height;

    @Column(name = "minimum_stock")
    private Integer minimumStock;

    @Column(nullable = false)
    private Boolean active;
}