package com.example.designpatterns.behavioral.strategy;

import java.math.BigDecimal;

/** Outcome of one payment. Every strategy returns the same shape. */
public record PaymentResult(String method, BigDecimal amount, BigDecimal fee, String reference) {

    public BigDecimal totalCharged() {
        return amount.add(fee);
    }
}
