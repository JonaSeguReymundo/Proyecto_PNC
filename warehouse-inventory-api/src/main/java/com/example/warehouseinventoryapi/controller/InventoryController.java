package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.PurchaseEntryRequest;
import com.example.warehouseinventoryapi.dto.response.BatchResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.MovementResponse;
import com.example.warehouseinventoryapi.dto.response.StockSummaryResponse;
import com.example.warehouseinventoryapi.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Gestion de inventario: entradas, lotes y stock")
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/entries")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar entrada de mercancia por compra")
    public BatchResponse registerEntry(@Valid @RequestBody PurchaseEntryRequest request) {
        return service.registerPurchaseEntry(request);
    }

    @GetMapping("/stock")
    @Operation(summary = "Consultar stock disponible de un producto en un almacen")
    public StockSummaryResponse getStock(@RequestParam Long productId,
                                         @RequestParam Long warehouseId) {
        return service.getStock(productId, warehouseId);
    }

    @GetMapping("/batches")
    @Operation(summary = "Listar lotes de un producto en un almacen (mas antiguos primero)")
    public List<BatchResponse> getBatches(@RequestParam Long productId,
                                          @RequestParam Long warehouseId) {
        return service.getBatches(productId, warehouseId);
    }

    @GetMapping("/movements")
    @Operation(summary = "Historial de movimientos de un producto")
    public PageableResponse<MovementResponse> getMovements(@RequestParam Long productId,
                                                           Pageable pageable) {
        return service.getMovements(productId, pageable);
    }
}