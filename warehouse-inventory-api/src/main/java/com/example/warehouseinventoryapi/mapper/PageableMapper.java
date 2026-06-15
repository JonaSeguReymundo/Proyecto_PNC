package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageableMapper {
    public <E, R> PageableResponse<R> toPageableResponse(Page<E> pageResult, List<R> content) {
        return PageableResponse.<R>builder()
                .content(content)
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .last(pageResult.isLast())
                .build();
    }
}