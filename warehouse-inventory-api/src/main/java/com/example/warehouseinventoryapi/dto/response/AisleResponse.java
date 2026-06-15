package com.example.warehouseinventoryapi.dto.response;

public record AisleResponse(
        Long id,
        String code,
        Long warehouseId
) {}