package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateLevelRequest(
        @NotNull(message = "Level number is required")
        @Positive(message = "Level number must be greater than zero")
        Integer number
) {}
