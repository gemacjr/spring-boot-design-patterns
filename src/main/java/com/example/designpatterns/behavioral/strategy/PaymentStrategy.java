package com.example.designpatterns.behavioral.strategy;

import java.math.BigDecimal;

/**
 * <b>Strategy</b>: one interchangeable algorithm for taking a payment.
 *
 * <p>Each implementation owns its own fee schedule. The alternative — a
 * {@code switch} on payment type inside a service — puts every rail's rules in
 * one method that must be edited for every new rail, and that grows a new branch
 * every quarter. Here, a new rail is a new class and nothing else changes.
 *
 * <p>{@link #key()} lets the strategy name itself, so the lookup table can be
 * built from the strategies rather than maintained alongside them.
 *
 * <p>Full documentation: {@code docs/patterns/strategy.md}
 */
public interface PaymentStrategy {

    /** The identifier callers use to select this strategy, e.g. {@code "paypal"}. */
    String key();

    PaymentResult pay(BigDecimal amount);
}
