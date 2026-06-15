package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTransferRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        // sourceWarehouseId puede ser nulo si el producto viene de un proveedor externo
        Long sourceWarehouseId,

        @NotNull(message = "Destination Warehouse ID is required")
        Long destinationWarehouseId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}