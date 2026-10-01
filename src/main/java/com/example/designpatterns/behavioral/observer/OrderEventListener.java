package com.example.designpatterns.behavioral.observer;

/**
 * <b>Observer</b>: notified when the subject publishes.
 *
 * <p>Full documentation: {@code docs/patterns/observer.md}
 */
@FunctionalInterface
public interface OrderEventListener {

    void onEvent(OrderEvent event);
}
