package com.example.designpatterns.behavioral.state;

/** <b>Concrete State</b>: terminal. Accepts nothing. */
public final class DeliveredState implements OrderState {

    public static final DeliveredState INSTANCE = new DeliveredState();

    private DeliveredState() {
    }

    @Override public String name() { return "DELIVERED"; }

    @Override public boolean terminal() { return true; }

    @Override
    public Transition on(OrderEventType event) {
        return refuse(event);
    }
}
