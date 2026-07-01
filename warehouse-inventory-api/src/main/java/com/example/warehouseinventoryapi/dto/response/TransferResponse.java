package com.example.warehouseinventoryapi.dto.response;

import java.time.LocalDateTime;

public record TransferResponse(
        Long id,
        Long productId,
        Long sourceWarehouseId,
        Long destinationWarehouseId,
        Integer quantity,
        LocalDateTime transferDate
) {}
