package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateLocationRequest(
        @NotBlank(message = "Code is required")
        String code,

        @NotNull(message = "Occupied status is required")
        Boolean occupied
) {}