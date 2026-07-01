package com.example.warehouseinventoryapi.inventory;

import com.example.warehouseinventoryapi.entity.InventoryBatch;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CostingCalculator {

    public BigDecimal weightedAverageCost(List<InventoryBatch> batches) {
        BigDecimal totalValue = BigDecimal.ZERO;
        long totalQuantity = 0;

        for (InventoryBatch b : batches) {
            int qty = b.getAvailableQuantity();
            if (qty <= 0) continue;
            totalValue = totalValue.add(b.getUnitCost().multiply(BigDecimal.valueOf(qty)));
            totalQuantity += qty;
        }

        if (totalQuantity == 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }

        return totalValue.divide(BigDecimal.valueOf(totalQuantity), 4, RoundingMode.HALF_UP);
    }

    public BigDecimal totalInventoryValue(List<InventoryBatch> batches) {
        BigDecimal total = BigDecimal.ZERO;
        for (InventoryBatch b : batches) {
            int qty = b.getAvailableQuantity();
            if (qty <= 0) continue;
            total = total.add(b.getUnitCost().multiply(BigDecimal.valueOf(qty)));
        }
        return total.setScale(4, RoundingMode.HALF_UP);
    }
}