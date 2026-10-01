package com.example.designpatterns.structural.facade;

import java.util.HashMap;
import java.util.Map;

/** One of three subsystems the facade hides. Knows nothing about the others. */
public class InventorySubsystem {

    private final Map<String, Integer> stock = new HashMap<>(Map.of(
            "WIDGET-1", 100,
            "GADGET-7", 12));

    public boolean reserve(String sku, int quantity) {
        int available = stock.getOrDefault(sku, 0);
        if (available < quantity) {
            return false;
        }
        stock.put(sku, available - quantity);
        return true;
    }

    public void release(String sku, int quantity) {
        stock.merge(sku, quantity, Integer::sum);
    }

    public int available(String sku) {
        return stock.getOrDefault(sku, 0);
    }
}
