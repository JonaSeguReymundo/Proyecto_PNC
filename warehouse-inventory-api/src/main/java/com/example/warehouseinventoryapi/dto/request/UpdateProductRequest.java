package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @Positive(message = "Weight must be positive")
        Double weight,

        @Positive(message = "Length must be positive")
        Double length,

        @Positive(message = "Width must be positive")
        Double width,

        @Positive(message = "Height must be positive")
        Double height,

        @PositiveOrZero(message = "Minimum stock cannot be negative")
        Integer minimumStock,

        Boolean active
) {}