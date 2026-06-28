package com.example.warehouseinventoryapi.inventory;

import com.example.warehouseinventoryapi.entity.InventoryBatch;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class FifoAllocator {

    public record Allocation(InventoryBatch batch, int quantityTaken) {}

    public List<Allocation> allocate(List<InventoryBatch> batchesOldestFirst, int quantityRequested) {
        if (quantityRequested <= 0) {
            throw new IllegalArgumentException("Quantity to withdraw must be positive");
        }

        int totalAvailable = batchesOldestFirst.stream()
                .mapToInt(InventoryBatch::getAvailableQuantity)
                .sum();

        if (quantityRequested > totalAvailable) {
            throw new IllegalArgumentException(
                    "Insufficient stock: requested " + quantityRequested
                            + " but only " + totalAvailable + " available");
        }

        List<Allocation> allocations = new ArrayList<>();
        int remaining = quantityRequested;

        for (InventoryBatch batch : batchesOldestFirst) {
            if (remaining <= 0) break;

            int available = batch.getAvailableQuantity();
            if (available <= 0) continue;

            int taken = Math.min(available, remaining);
            allocations.add(new Allocation(batch, taken));
            remaining -= taken;
        }

        return allocations;
    }
}