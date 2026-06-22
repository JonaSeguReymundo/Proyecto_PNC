package com.example.warehouseinventoryapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWarehouseRequest(
        @NotBlank(message = "Name is required")
        String name,

        String address,

        Boolean active
) {}