package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseEntryRequest(
        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Warehouse id is required")
        Long warehouseId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Integer quantity,

        @NotNull(message = "Unit cost is required")
        @Positive(message = "Unit cost must be positive")
        BigDecimal unitCost,

        String batchCode,

        LocalDate manufacturingDate,

        LocalDate expirationDate,

        String reference
) {}