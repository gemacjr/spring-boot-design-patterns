package com.example.designpatterns.behavioral.chainofresponsibility;

import java.util.Optional;

/**
 * <b>Handler</b>: one link in the validation chain.
 *
 * <p>Each handler owns exactly one rule and knows nothing about the others,
 * including whether any run after it. That ignorance is what lets the chain be
 * reordered or extended without touching a single existing handler.
 *
 * <p>{@link #order()} rather than a {@code next} pointer: the classic GoF form has
 * each handler hold a reference to its successor, which means the chain's shape is
 * scattered across its links. Declaring a sort key instead keeps handlers
 * independent and lets Spring assemble the chain from whatever beans exist.
 *
 * <p>Full documentation: {@code docs/patterns/chainofresponsibility.md}
 */
public interface ValidationHandler {

    /** Lower runs earlier. Handlers are sorted by this, never by injection order. */
    int order();

    /** Short name, reported in {@link ValidationResult#handlersRun()}. */
    String name();

    /**
     * @return empty if the input passes this rule, otherwise the reason it failed
     */
    Optional<String> validate(String input);
}
