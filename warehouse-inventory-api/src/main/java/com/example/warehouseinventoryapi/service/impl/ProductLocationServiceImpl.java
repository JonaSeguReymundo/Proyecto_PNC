package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateProductLocationRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductLocationResponse;
import com.example.warehouseinventoryapi.entity.ProductLocation;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.mapper.ProductLocationMapper;
import com.example.warehouseinventoryapi.repository.ProductLocationRepository;
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
    private final ProductLocationMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional
    public ProductLocationResponse create(CreateProductLocationRequest request) {
        ProductLocation productLocation = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(productLocation));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductLocationResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Record not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<ProductLocationResponse> getByProductId(Long productId, Pageable pageable) {
        Page<ProductLocation> pageResult = repository.findByProductId(productId, pageable);
        List<ProductLocationResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<ProductLocationResponse> getByLocationId(Long locationId, Pageable pageable) {
        Page<ProductLocation> pageResult = repository.findByLocationId(locationId, pageable);
        List<ProductLocationResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductLocationResponse getExactRecord(Long productId, Long locationId) {
        return repository.findByProductIdAndLocationId(productId, locationId).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found for Product " + productId + " at Location " + locationId));
    }
}