package com.example.warehouseinventoryapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BatchResponse(
        Long id,
        Long productId,
        String productSku,
        Long warehouseId,
        String batchCode,
        Integer initialQuantity,
        Integer availableQuantity,
        BigDecimal unitCost,
        LocalDate manufacturingDate,
        LocalDate expirationDate,
        LocalDateTime receivedAt
) {}