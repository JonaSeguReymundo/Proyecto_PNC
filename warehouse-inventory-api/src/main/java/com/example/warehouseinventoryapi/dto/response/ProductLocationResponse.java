package com.example.warehouseinventoryapi.dto.response;

public record ProductLocationResponse(
        Long id,
        Long productId,
        Long locationId,
        Integer quantity
) {}