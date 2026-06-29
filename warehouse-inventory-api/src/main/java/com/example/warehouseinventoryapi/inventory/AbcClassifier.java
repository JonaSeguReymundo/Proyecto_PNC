package com.example.warehouseinventoryapi.inventory;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class AbcClassifier {

    public record Item(Long productId, String sku, BigDecimal value) {}

    public record Classified(Long productId, String sku, BigDecimal value,
                             String abcClass, double cumulativePercentage) {}

    public List<Classified> classify(List<Item> items) {
        List<Classified> result = new ArrayList<>();
        if (items == null || items.isEmpty()) return result;

        BigDecimal total = items.stream()
                .map(Item::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            for (Item it : items) {
                result.add(new Classified(it.productId(), it.sku(), it.value(), "C", 0.0));
            }
            return result;
        }

        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(Item::value).reversed());

        BigDecimal cumulative = BigDecimal.ZERO;
        for (Item it : sorted) {
            cumulative = cumulative.add(it.value());
            double cumulativePct = cumulative
                    .divide(total, 4, java.math.RoundingMode.HALF_UP)
                    .doubleValue() * 100;

            String abcClass;
            if (cumulativePct <= 80.0) {
                abcClass = "A";
            } else if (cumulativePct <= 95.0) {
                abcClass = "B";
            } else {
                abcClass = "C";
            }

            result.add(new Classified(it.productId(), it.sku(), it.value(),
                    abcClass, Math.round(cumulativePct * 100.0) / 100.0));
        }

        return result;
    }
}