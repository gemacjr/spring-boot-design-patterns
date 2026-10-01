package com.example.designpatterns.behavioral.command;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>Receiver</b>: the object commands act upon.
 *
 * <p>It knows how to add, remove and discount. It knows nothing about undo,
 * history, or the existence of commands — which is exactly the separation the
 * pattern is for.
 *
 * <p>Full documentation: {@code docs/patterns/command.md}
 */
public class Cart {

    private final Map<String, Integer> items = new LinkedHashMap<>();
    private BigDecimal discountPercent = BigDecimal.ZERO;

    public void add(String sku, int quantity) {
        items.merge(sku, quantity, Integer::sum);
    }

    public void remove(String sku, int quantity) {
        items.computeIfPresent(sku, (key, current) -> {
            int left = current - quantity;
            return left <= 0 ? null : left;
        });
    }

    public void setDiscountPercent(BigDecimal percent) {
        this.discountPercent = percent;
    }

    public BigDecimal discountPercent() {
        return discountPercent;
    }

    public Map<String, Integer> items() {
        return Map.copyOf(items);
    }

    /** A value snapshot, so tests can assert an exact round trip. */
    public String snapshot() {
        return items + " discount=" + discountPercent;
    }
}
