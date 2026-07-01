package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.response.AbcReportItemResponse;
import com.example.warehouseinventoryapi.dto.response.ProductCostResponse;
import com.example.warehouseinventoryapi.dto.response.RopResponse;
import com.example.warehouseinventoryapi.dto.response.StockAlertResponse;
import com.example.warehouseinventoryapi.entity.InventoryBatch;
import com.example.warehouseinventoryapi.entity.Product;
import com.example.warehouseinventoryapi.exception.BadRequestException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.inventory.AbcClassifier;
import com.example.warehouseinventoryapi.inventory.CostingCalculator;
import com.example.warehouseinventoryapi.inventory.RopCalculator;
import com.example.warehouseinventoryapi.repository.InventoryBatchRepository;
import com.example.warehouseinventoryapi.repository.ProductRepository;
import com.example.warehouseinventoryapi.repository.WarehouseRepository;
import com.example.warehouseinventoryapi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ProductRepository        productRepository;
    private final InventoryBatchRepository  batchRepository;
    private final WarehouseRepository       warehouseRepository;
    private final CostingCalculator         costingCalculator;
    private final RopCalculator             ropCalculator;
    private final AbcClassifier             abcClassifier;

    @Override
    @Transactional(readOnly = true)
    public List<StockAlertResponse> getStockAlerts(Long warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
        }

        List<StockAlertResponse> alerts = new ArrayList<>();

        for (Product product : productRepository.findAllByActiveTrue()) {
            int currentStock = currentStock(product.getId(), warehouseId);
            int minimum = product.getMinimumStock() != null ? product.getMinimumStock() : 0;

            if (currentStock < minimum) {
                alerts.add(new StockAlertResponse(
                        product.getId(),
                        product.getSku(),
                        product.getName(),
                        currentStock,
                        minimum,
                        minimum - currentStock
                ));
            }
        }
        return alerts;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCostResponse getProductCost(Long productId, Long warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<InventoryBatch> batches = batchRepository
                .findByProductIdAndWarehouseIdOrderByReceivedAtAsc(productId, warehouseId);

        int totalQty = batches.stream().mapToInt(InventoryBatch::getAvailableQuantity).sum();
        BigDecimal avgCost = costingCalculator.weightedAverageCost(batches);
        BigDecimal totalValue = costingCalculator.totalInventoryValue(batches);

        return new ProductCostResponse(productId, product.getSku(), totalQty, avgCost, totalValue);
    }

    @Override
    @Transactional(readOnly = true)
    public RopResponse getReorderPoint(Long productId, Long warehouseId,
                                       double averageDailyDemand, int leadTimeDays, int safetyStock) {
        if (averageDailyDemand < 0) {
            throw new BadRequestException("Average daily demand cannot be negative: " + averageDailyDemand);
        }
        if (leadTimeDays < 0) {
            throw new BadRequestException("Lead time days cannot be negative: " + leadTimeDays);
        }
        if (safetyStock < 0) {
            throw new BadRequestException("Safety stock cannot be negative: " + safetyStock);
        }

        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int currentStock = currentStock(productId, warehouseId);
        int rop = ropCalculator.reorderPoint(averageDailyDemand, leadTimeDays, safetyStock);
        boolean shouldReorder = ropCalculator.shouldReorder(currentStock, rop);

        return new RopResponse(productId, product.getSku(), currentStock,
                averageDailyDemand, leadTimeDays, safetyStock, rop, shouldReorder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbcReportItemResponse> getAbcReport(Long warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
        }

        List<AbcClassifier.Item> items = new ArrayList<>();

        for (Product product : productRepository.findAllByActiveTrue()) {
            List<InventoryBatch> batches = batchRepository
                    .findByProductIdAndWarehouseIdOrderByReceivedAtAsc(product.getId(), warehouseId);
            BigDecimal value = costingCalculator.totalInventoryValue(batches);
            items.add(new AbcClassifier.Item(product.getId(), product.getSku(), value));
        }

        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        List<AbcClassifier.Classified> classified = abcClassifier.classify(items);

        List<AbcReportItemResponse> result = new ArrayList<>();
        for (AbcClassifier.Classified c : classified) {
            result.add(new AbcReportItemResponse(
                    c.productId(), c.sku(), c.value(), c.abcClass(), c.cumulativePercentage()));
        }
        return result;
    }

    private int currentStock(Long productId, Long warehouseId) {
        return batchRepository
                .findByProductIdAndWarehouseIdOrderByReceivedAtAsc(productId, warehouseId)
                .stream()
                .mapToInt(InventoryBatch::getAvailableQuantity)
                .sum();
    }
}