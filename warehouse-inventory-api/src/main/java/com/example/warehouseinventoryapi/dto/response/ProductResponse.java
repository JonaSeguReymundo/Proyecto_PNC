package com.example.warehouseinventoryapi.dto.response;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        Double weight,
        Double length,
        Double width,
        Double height,
        Integer minimumStock,
        Boolean active
) {}
