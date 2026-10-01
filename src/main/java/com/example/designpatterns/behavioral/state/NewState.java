package com.example.designpatterns.behavioral.state;

/** <b>Concrete State</b>: created, not yet paid. May be paid or cancelled. */
public final class NewState implements OrderState {

    public static final NewState INSTANCE = new NewState();

    private NewState() {
    }

    @Override public String name() { return "NEW"; }

    @Override public boolean terminal() { return false; }

    @Override
    public Transition on(OrderEventType event) {
        return switch (event) {
            case PAY -> accept(PaidState.INSTANCE);
            case CANCEL -> accept(CancelledState.INSTANCE);
            case SHIP, DELIVER -> refuse(event);
        };
    }
}
