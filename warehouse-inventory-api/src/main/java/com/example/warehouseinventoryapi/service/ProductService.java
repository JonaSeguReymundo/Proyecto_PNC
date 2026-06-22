package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateProductRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateProductRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);
    ProductResponse update(Long id, UpdateProductRequest request);
    ProductResponse getById(Long id);
    void delete(Long id);
    ProductResponse getBySku(String sku);
    PageableResponse<ProductResponse> getAllActive(Pageable pageable);
}