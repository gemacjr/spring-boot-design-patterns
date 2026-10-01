package com.example.designpatterns.behavioral.state;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Context</b>: holds the current state and delegates every event to it.
 *
 * <p>Notice what is absent: no {@code if}, no {@code switch} on status, no
 * transition table. The context does not know the rules; the states do. Adding a
 * state means adding a class, not editing this one.
 *
 * <p>Full documentation: {@code docs/patterns/state.md}
 */
public class Order {

    private final String id;
    private final List<String> history = new ArrayList<>();
    private OrderState state = NewState.INSTANCE;

    public Order(String id) {
        this.id = id;
        history.add("NEW");
    }

    public String id() {
        return id;
    }

    public OrderState state() {
        return state;
    }

    public List<String> history() {
        return List.copyOf(history);
    }

    /**
     * Asks the current state to handle {@code event}.
     *
     * <p>A refused transition leaves the state untouched and returns
     * {@code accepted=false} with the reason — see {@link TransitionResult}.
     */
    public TransitionResult handle(OrderEventType event) {
        String from = state.name();
        OrderState.Transition transition = state.on(event);

        if (!transition.accepted()) {
            return new TransitionResult(false, from, from, transition.reason());
        }

        state = transition.next();
        history.add(state.name());
        return new TransitionResult(true, from, state.name(), "ok");
    }
}
