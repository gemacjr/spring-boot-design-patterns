package com.example.designpatterns.behavioral.state;

/**
 * The public outcome of asking an order to handle an event.
 *
 * <p>A refusal is an ordinary value, not an exception: "you cannot cancel a
 * shipped order" is a business answer the caller will want to show a user, and
 * making it an exception would force a try/catch around routine control flow.
 * When {@code accepted} is false, {@code from} and {@code to} are equal.
 */
public record TransitionResult(boolean accepted, String from, String to, String reason) {
}
