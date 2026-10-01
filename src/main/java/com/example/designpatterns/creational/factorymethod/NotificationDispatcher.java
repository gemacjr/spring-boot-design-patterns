package com.example.designpatterns.creational.factorymethod;

/**
 * <b>Creator</b> in the Factory Method pattern.
 *
 * <p>{@link #dispatch} is the invariant part of the algorithm &mdash; validation,
 * then delivery &mdash; written exactly once. Subclasses override only
 * {@link #createChannel()} to decide <em>which</em> product the algorithm operates
 * on. Adding a channel means adding one subclass and changing nothing here, which
 * is the Open/Closed Principle in action.
 *
 * <p>{@code dispatch} is {@code final} deliberately: subclasses vary the product,
 * never the algorithm. That is the line between Factory Method and Template Method.
 *
 * <p>Full documentation: {@code docs/patterns/factorymethod.md}
 */
public abstract class NotificationDispatcher {

    /** The factory method. Subclasses decide the concrete product. */
    protected abstract NotificationChannel createChannel();

    public final String dispatch(String to, String message) {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("recipient must not be blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        return createChannel().send(to.trim(), message);
    }
}
