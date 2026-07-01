package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateProductRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateProductRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductResponse;
import com.example.warehouseinventoryapi.entity.Product;
import com.example.warehouseinventoryapi.exception.DuplicateResourceException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.mapper.ProductMapper;
import com.example.warehouseinventoryapi.repository.ProductRepository;
import com.example.warehouseinventoryapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper     mapper;
    private final PageableMapper    pageableMapper;

    @Override
    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        if (repository.existsBySku(request.sku())) {
            throw new DuplicateResourceException("Product with SKU '" + request.sku() + "' already exists in the system.");
        }

        Product product = mapper.toEntityCreate(request);
        product.setActive(true);

        return mapper.toDto(repository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        mapper.updateEntityFromDto(request, existingProduct);
        return mapper.toDto(repository.save(existingProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        repository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getBySku(String sku) {
        return repository.findBySku(sku).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<ProductResponse> getAllActive(Pageable pageable) {
        Page<Product> pageResult = repository.findAllByActiveTrue(pageable);
        List<ProductResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }
}