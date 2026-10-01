package com.example.designpatterns.structural.facade;

import java.math.BigDecimal;
import java.util.UUID;

/** One of three subsystems the facade hides. */
public class PaymentSubsystem {

    /** Any card number starting with FAIL is declined, so the failure path is demonstrable. */
    public String charge(String card, BigDecimal amount) {
        if (card == null || card.startsWith("FAIL")) {
            throw new IllegalStateException("payment declined for card " + card);
        }
        return "txn-" + UUID.randomUUID().toString().substring(0, 8) + " for " + amount;
    }
}
