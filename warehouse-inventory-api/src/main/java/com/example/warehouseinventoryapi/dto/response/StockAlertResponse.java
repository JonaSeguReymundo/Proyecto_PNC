package com.example.warehouseinventoryapi.dto.response;

public record StockAlertResponse(
        Long productId,
        String productSku,
        String productName,
        Integer currentStock,
        Integer minimumStock,
        Integer deficit
) {}
