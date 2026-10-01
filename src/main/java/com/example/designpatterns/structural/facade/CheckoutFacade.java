package com.example.designpatterns.structural.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <b>Facade</b>: one entry point over three subsystems.
 *
 * <p>Without it, every caller would need to know the correct call order, the
 * pricing rule, and — the part that actually bites — how to unwind a reservation
 * when payment fails. That knowledge would be copy-pasted into every caller and
 * would drift.
 *
 * <p>A facade does not <em>forbid</em> direct subsystem access; it just means
 * nobody normally needs it. Keep the subsystems usable on their own, or the facade
 * becomes a bottleneck instead of a convenience.
 *
 * <p>Full documentation: {@code docs/patterns/facade.md}
 */
public class CheckoutFacade {

    private static final BigDecimal UNIT_PRICE = new BigDecimal("19.99");

    private final InventorySubsystem inventory;
    private final PaymentSubsystem payment;
    private final ShippingSubsystem shipping;

    public CheckoutFacade() {
        this(new InventorySubsystem(), new PaymentSubsystem(), new ShippingSubsystem());
    }

    public CheckoutFacade(InventorySubsystem inventory, PaymentSubsystem payment,
                          ShippingSubsystem shipping) {
        this.inventory = inventory;
        this.payment = payment;
        this.shipping = shipping;
    }

    public CheckoutResult checkout(String sku, int quantity, String card, String address) {
        List<String> steps = new ArrayList<>();
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        steps.add("inventory.reserve");
        if (!inventory.reserve(sku, quantity)) {
            return new CheckoutResult(false, orderId,
                    "Insufficient stock for " + sku + " (requested " + quantity
                            + ", available " + inventory.available(sku) + ")",
                    steps);
        }

        steps.add("payment.charge");
        String transaction;
        try {
            transaction = payment.charge(card, UNIT_PRICE.multiply(BigDecimal.valueOf(quantity)));
        } catch (IllegalStateException declined) {
            // The unwind the caller would otherwise have to remember.
            steps.add("inventory.release");
            inventory.release(sku, quantity);
            return new CheckoutResult(false, orderId, "Checkout failed: " + declined.getMessage(), steps);
        }

        steps.add("shipping.schedule");
        String tracking = shipping.schedule(orderId, address);

        return new CheckoutResult(true, orderId,
                "Order placed. " + transaction + ", tracking " + tracking, steps);
    }
}
