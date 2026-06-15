package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductLocationRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Location ID is required")
        Long locationId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}
