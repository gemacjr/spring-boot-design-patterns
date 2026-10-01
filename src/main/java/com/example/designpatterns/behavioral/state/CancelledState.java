package com.example.designpatterns.behavioral.state;

/** <b>Concrete State</b>: terminal. Accepts nothing. */
public final class CancelledState implements OrderState {

    public static final CancelledState INSTANCE = new CancelledState();

    private CancelledState() {
    }

    @Override public String name() { return "CANCELLED"; }

    @Override public boolean terminal() { return true; }

    @Override
    public Transition on(OrderEventType event) {
        return refuse(event);
    }
}
