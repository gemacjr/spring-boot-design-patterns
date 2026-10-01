package com.example.designpatterns.behavioral.chainofresponsibility;

import java.util.List;

/**
 * Outcome of running the chain.
 *
 * <p>{@code handlersRun} makes the traversal observable — without it, "did the
 * chain stop early?" is unanswerable from the outside.
 */
public record ValidationResult(boolean valid, List<String> failures, List<String> handlersRun) {

    public ValidationResult {
        failures = List.copyOf(failures);
        handlersRun = List.copyOf(handlersRun);
    }
}
