package com.example.warehouseinventoryapi.dto.request;

import com.example.warehouseinventoryapi.entity.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdateUserRequest(
        @NotBlank(message = "Full name is required")
        String fullName,

        @NotEmpty(message = "At least one role is required")
        Set<RoleName> roles,

        Boolean active
) {}
