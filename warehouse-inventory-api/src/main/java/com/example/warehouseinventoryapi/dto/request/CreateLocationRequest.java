package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLocationRequest(
        @NotBlank(message = "Code is required")
        String code,

        @NotNull(message = "Level ID is required")
        Long levelId
) {}