package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateProductLocationRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductLocationResponse;
import com.example.warehouseinventoryapi.entity.ProductLocation;
import com.example.warehouseinventoryapi.exception.DuplicateResourceException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.mapper.ProductLocationMapper;
import com.example.warehouseinventoryapi.repository.LocationRepository; // Added for parent validation
import com.example.warehouseinventoryapi.repository.ProductLocationRepository;
import com.example.warehouseinventoryapi.repository.ProductRepository;  // Added for parent validation
import com.example.warehouseinventoryapi.service.ProductLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductLocationServiceImpl implements ProductLocationService {

    private final ProductLocationRepository repository;
    private final ProductRepository         productRepository;  // Injected for structural validation
    private final LocationRepository        locationRepository; // Injected for structural validation
    private final ProductLocationMapper     mapper;
    private final PageableMapper            pageableMapper;

    @Override
    @Transactional
    public ProductLocationResponse create(CreateProductLocationRequest request) {
        if (!productRepository.existsById(request.productId())) {
            throw new ResourceNotFoundException("Product not found with id: " + request.productId());
        }

        if (!locationRepository.existsById(request.locationId())) {
            throw new ResourceNotFoundException("Location not found with id: " + request.locationId());
        }

        if (repository.existsByProductIdAndLocationId(request.productId(), request.locationId())) {
            throw new DuplicateResourceException("Product with id " + request.productId() +
                    " is already assigned to location with id " + request.locationId());
        }

        ProductLocation productLocation = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(productLocation));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductLocationResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product-Location assignment record not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProductLocation productLocation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product-Location assignment record not found with id: " + id));

        repository.delete(productLocation);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<ProductLocationResponse> getByProductId(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Page<ProductLocation> pageResult = repository.findByProductId(productId, pageable);
        List<ProductLocationResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<ProductLocationResponse> getByLocationId(Long locationId, Pageable pageable) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with id: " + locationId);
        }

        Page<ProductLocation> pageResult = repository.findByLocationId(locationId, pageable);
        List<ProductLocationResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductLocationResponse getExactRecord(Long productId, Long locationId) {
        return repository.findByProductIdAndLocationId(productId, locationId).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment details not found for Product ID "
                        + productId + " at Location ID " + locationId));
    }
}