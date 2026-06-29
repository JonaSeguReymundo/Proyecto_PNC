package com.example.warehouseinventoryapi.inventory;

import org.springframework.stereotype.Component;

@Component
public class RopCalculator {

    public int reorderPoint(double averageDailyDemand, int leadTimeDays, int safetyStock) {
        if (averageDailyDemand < 0 || leadTimeDays < 0 || safetyStock < 0) {
            throw new IllegalArgumentException("ROP inputs cannot be negative");
        }
        double rop = (averageDailyDemand * leadTimeDays) + safetyStock;
        return (int) Math.ceil(rop);
    }

    public boolean shouldReorder(int currentStock, int reorderPoint) {
        return currentStock <= reorderPoint;
    }
}