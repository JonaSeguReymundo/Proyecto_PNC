package com.example.warehouseinventoryapi.dto.response;

import com.example.warehouseinventoryapi.entity.RoleName;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        Boolean active,
        Set<RoleName> roles
) {}
