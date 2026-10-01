package com.example.designpatterns.behavioral.state;

/** <b>Concrete State</b>: paid, awaiting dispatch. May be shipped or cancelled. */
public final class PaidState implements OrderState {

    public static final PaidState INSTANCE = new PaidState();

    private PaidState() {
    }

    @Override public String name() { return "PAID"; }

    @Override public boolean terminal() { return false; }

    @Override
    public Transition on(OrderEventType event) {
        return switch (event) {
            case SHIP -> accept(ShippedState.INSTANCE);
            case CANCEL -> accept(CancelledState.INSTANCE);
            case PAY, DELIVER -> refuse(event);
        };
    }
}
