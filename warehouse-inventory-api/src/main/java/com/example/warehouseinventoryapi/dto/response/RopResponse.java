package com.example.warehouseinventoryapi.dto.response;

public record RopResponse(
        Long productId,
        String productSku,
        Integer currentStock,
        double averageDailyDemand,
        int leadTimeDays,
        int safetyStock,
        int reorderPoint,
        boolean shouldReorder
) {}
