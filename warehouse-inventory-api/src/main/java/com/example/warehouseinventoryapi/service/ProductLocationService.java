package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateProductLocationRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductLocationResponse;
import org.springframework.data.domain.Pageable;

public interface ProductLocationService {
    ProductLocationResponse create(CreateProductLocationRequest request);
    ProductLocationResponse getById(Long id);
    void delete(Long id);
    PageableResponse<ProductLocationResponse> getByProductId(Long productId, Pageable pageable);
    PageableResponse<ProductLocationResponse> getByLocationId(Long locationId, Pageable pageable);
    ProductLocationResponse getExactRecord(Long productId, Long locationId);
}