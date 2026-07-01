package com.example.warehouseinventoryapi.dto.response;

public record RackResponse(
        Long id,
        String code,
        Long aisleId
) {}