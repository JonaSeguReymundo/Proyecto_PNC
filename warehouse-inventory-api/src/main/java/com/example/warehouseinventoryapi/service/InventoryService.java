package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.PurchaseEntryRequest;
import com.example.warehouseinventoryapi.dto.request.OrderExitRequest;
import com.example.warehouseinventoryapi.dto.response.BatchResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.MovementResponse;
import com.example.warehouseinventoryapi.dto.response.StockSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {

    BatchResponse registerPurchaseEntry(PurchaseEntryRequest request);

    StockSummaryResponse getStock(Long productId, Long warehouseId);

    List<BatchResponse> getBatches(Long productId, Long warehouseId);

    PageableResponse<MovementResponse> getMovements(Long productId, Pageable pageable);

    List<MovementResponse> registerOrderExit(OrderExitRequest request);
}