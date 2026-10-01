package com.example.designpatterns.behavioral.observer;

import java.time.Instant;

/**
 * The notification passed from subject to observers.
 *
 * <p>Immutable on purpose: the same instance is handed to every listener, so a
 * mutable event would let one listener corrupt what the others see.
 *
 * <p>Full documentation: {@code docs/patterns/observer.md}
 */
public record OrderEvent(String orderId, String type, Instant at) {

    public static OrderEvent of(String orderId, String type) {
        return new OrderEvent(orderId, type, Instant.now());
    }
}
