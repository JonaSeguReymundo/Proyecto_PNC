package com.example.warehouseinventoryapi.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PageableResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
}
