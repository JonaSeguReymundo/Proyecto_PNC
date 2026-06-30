package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateTransferRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.TransferResponse;
import com.example.warehouseinventoryapi.entity.Transfer;
import com.example.warehouseinventoryapi.exception.BadRequestException; // Added for business rules
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.mapper.TransferMapper;
import com.example.warehouseinventoryapi.repository.ProductRepository;   // Added for verification
import com.example.warehouseinventoryapi.repository.TransferRepository;
import com.example.warehouseinventoryapi.repository.WarehouseRepository; // Added for verification
import com.example.warehouseinventoryapi.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository  repository;
    private final ProductRepository   productRepository;   // Injected for target validation
    private final WarehouseRepository warehouseRepository; // Injected for scope validation
    private final TransferMapper      mapper;
    private final PageableMapper      pageableMapper;

    @Override
    @Transactional
    public TransferResponse create(CreateTransferRequest request) {
        if (request.quantity() <= 0) {
            throw new BadRequestException("Transfer quantity must be greater than zero.");
        }

        if (request.sourceWarehouseId().equals(request.destinationWarehouseId())) {
            throw new BadRequestException("Source and destination warehouses must be different resource units.");
        }

        if (!productRepository.existsById(request.productId())) {
            throw new ResourceNotFoundException("Product not found with id: " + request.productId());
        }

        if (!warehouseRepository.existsById(request.sourceWarehouseId())) {
            throw new ResourceNotFoundException("Source warehouse not found with id: " + request.sourceWarehouseId());
        }

        if (!warehouseRepository.existsById(request.destinationWarehouseId())) {
            throw new ResourceNotFoundException("Destination warehouse not found with id: " + request.destinationWarehouseId());
        }

        Transfer transfer = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(transfer));
    }

    @Override
    @Transactional(readOnly = true)
    public TransferResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer record not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<TransferResponse> getByProductId(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Page<Transfer> pageResult = repository.findByProductId(productId, pageable);
        List<TransferResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<TransferResponse> getBySourceWarehouseId(Long warehouseId, Pageable pageable) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Source warehouse not found with id: " + warehouseId);
        }

        Page<Transfer> pageResult = repository.findBySourceWarehouseId(warehouseId, pageable);
        List<TransferResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<TransferResponse> getByDestinationWarehouseId(Long warehouseId, Pageable pageable) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Destination warehouse not found with id: " + warehouseId);
        }

        Page<Transfer> pageResult = repository.findByDestinationWarehouseId(warehouseId, pageable);
        List<TransferResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }
}