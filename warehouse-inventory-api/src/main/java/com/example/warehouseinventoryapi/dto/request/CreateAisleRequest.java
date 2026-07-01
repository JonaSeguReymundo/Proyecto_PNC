package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAisleRequest(
        @NotBlank(message = "Code is required")
        String code,

        @NotNull(message = "Warehouse ID is required")
        Long warehouseId
) {}
