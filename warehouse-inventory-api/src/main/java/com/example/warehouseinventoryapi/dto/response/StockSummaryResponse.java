package com.example.warehouseinventoryapi.dto.response;

public record StockSummaryResponse(
        Long productId,
        String productSku,
        Long warehouseId,
        Integer totalAvailable,
        int batchCount
) {}