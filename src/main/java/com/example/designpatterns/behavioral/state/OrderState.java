package com.example.designpatterns.behavioral.state;

/**
 * <b>State</b>: one condition an order can be in, and the transitions legal from it.
 *
 * <p>The pattern replaces a growing {@code if (status == …)} tangle with one class
 * per state. The win is that each state's rules live together, and adding a state
 * cannot break the others.
 *
 * <p>The interface is {@code sealed}, so the set of states is closed and the
 * compiler knows every one of them. On Java 21+ that additionally makes a
 * {@code switch} over an {@code OrderState} exhaustive without a {@code default}
 * branch — add a state and every switch stops compiling until it is handled. On
 * Java 17 (this project) sealing still documents and enforces the closed set;
 * pattern matching for {@code switch} was a preview feature until 21.
 *
 * <p>Each implementation is stateless, so each is a singleton — see the Singleton
 * package for why that is safe here and dangerous elsewhere.
 *
 * <p>Full documentation: {@code docs/patterns/state.md}
 */
public sealed interface OrderState
        permits NewState, PaidState, ShippedState, DeliveredState, CancelledState {

    String name();

    /** Whether this state accepts no further events. */
    boolean terminal();

    /** Decides what this state does with an event. */
    Transition on(OrderEventType event);

    /** Internal move: whether it was accepted, where it lands, and why not if refused. */
    record Transition(boolean accepted, OrderState next, String reason) {
    }

    /** Accepted move to {@code next}. */
    default Transition accept(OrderState next) {
        return new Transition(true, next, "ok");
    }

    /** Refused move: the order stays exactly where it was. */
    default Transition refuse(OrderEventType event) {
        return new Transition(false, this,
                "cannot " + event + " an order in state " + name());
    }
}
