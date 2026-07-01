package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.response.AbcReportItemResponse;
import com.example.warehouseinventoryapi.dto.response.ProductCostResponse;
import com.example.warehouseinventoryapi.dto.response.RopResponse;
import com.example.warehouseinventoryapi.dto.response.StockAlertResponse;
import com.example.warehouseinventoryapi.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Reportes de inventario: alertas, ROP, costeo y ABC")
public class ReportController {

    private final ReportService service;

    @GetMapping("/stock-alerts")
    @Operation(summary = "Productos por debajo del stock minimo en un almacen")
    public List<StockAlertResponse> getStockAlerts(@RequestParam Long warehouseId) {
        return service.getStockAlerts(warehouseId);
    }

    @GetMapping("/cost")
    @Operation(summary = "Costo promedio ponderado y valor de inventario de un producto")
    public ProductCostResponse getProductCost(@RequestParam Long productId,
                                              @RequestParam Long warehouseId) {
        return service.getProductCost(productId, warehouseId);
    }

    @GetMapping("/reorder-point")
    @Operation(summary = "Calcula el punto de reorden (ROP) de un producto")
    public RopResponse getReorderPoint(@RequestParam Long productId,
                                       @RequestParam Long warehouseId,
                                       @RequestParam double averageDailyDemand,
                                       @RequestParam int leadTimeDays,
                                       @RequestParam(defaultValue = "0") int safetyStock) {
        return service.getReorderPoint(productId, warehouseId,
                averageDailyDemand, leadTimeDays, safetyStock);
    }

    @GetMapping("/abc")
    @Operation(summary = "Reporte de clasificacion ABC de inventario")
    public List<AbcReportItemResponse> getAbcReport(@RequestParam Long warehouseId) {
        return service.getAbcReport(warehouseId);
    }
}
