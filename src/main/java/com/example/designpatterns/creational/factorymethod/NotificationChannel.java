package com.example.designpatterns.creational.factorymethod;

/**
 * <b>Product</b> in the Factory Method pattern.
 *
 * <p>A single abstract method, so a channel can also be supplied as a lambda.
 *
 * <p>Full documentation: {@code docs/patterns/factorymethod.md}
 */
@FunctionalInterface
public interface NotificationChannel {

    /** Delivers the message and returns a human-readable transcript of what was sent. */
    String send(String to, String message);
}
