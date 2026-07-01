package com.example.warehouseinventoryapi.dto.response;

public record LocationResponse(
        Long id,
        String code,
        Boolean occupied,
        Long levelId
) {}
