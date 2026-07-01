package com.example.warehouseinventoryapi.dto.response;

import com.example.warehouseinventoryapi.entity.MovementType;
import java.time.LocalDateTime;

public record MovementResponse(
        Long id,
        Long batchId,
        Long productId,
        Long warehouseId,
        MovementType type,
        Integer quantity,
        String performedBy,
        LocalDateTime createdAt,
        String reference
) {}