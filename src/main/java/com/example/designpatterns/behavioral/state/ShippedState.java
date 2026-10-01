package com.example.designpatterns.behavioral.state;

/**
 * <b>Concrete State</b>: in transit.
 *
 * <p>Cancellation is refused here by design: once goods are with the carrier,
 * cancelling is a returns process, not a state transition. Modelling it as a
 * refusal keeps the machine honest about what the business can actually do.
 */
public final class ShippedState implements OrderState {

    public static final ShippedState INSTANCE = new ShippedState();

    private ShippedState() {
    }

    @Override public String name() { return "SHIPPED"; }

    @Override public boolean terminal() { return false; }

    @Override
    public Transition on(OrderEventType event) {
        return switch (event) {
            case DELIVER -> accept(DeliveredState.INSTANCE);
            case PAY, SHIP, CANCEL -> refuse(event);
        };
    }
}
