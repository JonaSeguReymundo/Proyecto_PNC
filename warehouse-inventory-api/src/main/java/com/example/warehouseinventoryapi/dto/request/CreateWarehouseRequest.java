package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateWarehouseRequest(
        @NotBlank(message = "Name is required")
        String name,

        String address
) {}