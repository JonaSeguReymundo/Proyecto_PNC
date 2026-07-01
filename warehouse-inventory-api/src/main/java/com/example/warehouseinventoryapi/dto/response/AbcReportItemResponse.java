package com.example.warehouseinventoryapi.dto.response;

import java.math.BigDecimal;

public record AbcReportItemResponse(
        Long productId,
        String productSku,
        BigDecimal inventoryValue,
        String abcClass,
        double cumulativePercentage
) {}
