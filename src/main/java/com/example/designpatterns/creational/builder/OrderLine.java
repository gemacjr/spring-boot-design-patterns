package com.example.designpatterns.creational.builder;

import java.math.BigDecimal;

/**
 * One line of an {@link OrderRequest}.
 *
 * <p>A record: immutable, value-based, and validated in its compact constructor,
 * so an invalid line cannot exist even momentarily.
 *
 * <p>Full documentation: {@code docs/patterns/builder.md}
 */
public record OrderLine(String sku, int quantity, BigDecimal unitPrice) {

    public OrderLine {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive, was " + quantity);
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must not be negative");
        }
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
