package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRackRequest(
        @NotBlank(message = "Code is required")
        String code,

        @NotNull(message = "Aisle ID is required")
        Long aisleId
) {}