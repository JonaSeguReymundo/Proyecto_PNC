package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAisleRequest(
        @NotBlank(message = "Code is required")
        String code
) {}
