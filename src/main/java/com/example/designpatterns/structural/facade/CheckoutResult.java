package com.example.designpatterns.structural.facade;

import java.util.List;

/**
 * What the caller gets back from one facade call.
 *
 * <p>{@code stepsTaken} exists so the orchestration is observable — both for the
 * tests and for anyone reading the demo output.
 */
public record CheckoutResult(boolean success, String orderId, String message, List<String> stepsTaken) {

    public CheckoutResult {
        stepsTaken = List.copyOf(stepsTaken);
    }
}
