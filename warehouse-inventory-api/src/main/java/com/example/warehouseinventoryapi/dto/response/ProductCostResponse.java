package com.example.warehouseinventoryapi.dto.response;

import java.math.BigDecimal;

public record ProductCostResponse(
        Long productId,
        String productSku,
        Integer totalQuantity,
        BigDecimal weightedAverageCost,
        BigDecimal totalInventoryValue
) {}
