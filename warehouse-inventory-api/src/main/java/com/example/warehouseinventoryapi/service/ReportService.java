package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.response.AbcReportItemResponse;
import com.example.warehouseinventoryapi.dto.response.ProductCostResponse;
import com.example.warehouseinventoryapi.dto.response.RopResponse;
import com.example.warehouseinventoryapi.dto.response.StockAlertResponse;

import java.util.List;

public interface ReportService {

    List<StockAlertResponse> getStockAlerts(Long warehouseId);

    ProductCostResponse getProductCost(Long productId, Long warehouseId);

    RopResponse getReorderPoint(Long productId, Long warehouseId,
                                double averageDailyDemand, int leadTimeDays, int safetyStock);

    List<AbcReportItemResponse> getAbcReport(Long warehouseId);
}
