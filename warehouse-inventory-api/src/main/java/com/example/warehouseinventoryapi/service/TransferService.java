package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateTransferRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.TransferResponse;
import org.springframework.data.domain.Pageable;

public interface TransferService {
    TransferResponse create(CreateTransferRequest request);
    TransferResponse getById(Long id);
    PageableResponse<TransferResponse> getByProductId(Long productId, Pageable pageable);
    PageableResponse<TransferResponse> getBySourceWarehouseId(Long warehouseId, Pageable pageable);
    PageableResponse<TransferResponse> getByDestinationWarehouseId(Long warehouseId, Pageable pageable);
}