package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateReservationRequest(
        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Warehouse id is required")
        Long warehouseId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Integer quantity,

        String reference
) {}