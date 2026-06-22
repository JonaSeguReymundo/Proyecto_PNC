package com.example.warehouseinventoryapi.dto.response;

public record WarehouseResponse(
        Long id,
        String name,
        String address,
        Boolean active
) {}
