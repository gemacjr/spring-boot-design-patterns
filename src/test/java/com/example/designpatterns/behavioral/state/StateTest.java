package com.example.designpatterns.behavioral.state;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.designpatterns.behavioral.state.OrderEventType.CANCEL;
import static com.example.designpatterns.behavioral.state.OrderEventType.DELIVER;
import static com.example.designpatterns.behavioral.state.OrderEventType.PAY;
import static com.example.designpatterns.behavioral.state.OrderEventType.SHIP;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee State buys: the legal transitions are the only ones that
 * can happen, and an illegal one is refused without corrupting the object.
 */
class StateTest {

    @Test
    void theHappyPathIsAccepted() {
        Order order = new Order("ORD-1");

        assertThat(order.handle(PAY).accepted()).isTrue();
        assertThat(order.handle(SHIP).accepted()).isTrue();
        assertThat(order.handle(DELIVER).accepted()).isTrue();

        assertThat(order.state().name()).isEqualTo("DELIVERED");
        assertThat(order.history()).containsExactly("NEW", "PAID", "SHIPPED", "DELIVERED");
    }

    @Test
    void aNewOrderCanBeCancelled() {
        Order order = new Order("ORD-1");

        assertThat(order.handle(CANCEL).accepted()).isTrue();
        assertThat(order.state().name()).isEqualTo("CANCELLED");
    }

    @Test
    void aPaidOrderCanStillBeCancelled() {
        Order order = new Order("ORD-1");
        order.handle(PAY);

        assertThat(order.handle(CANCEL).accepted()).isTrue();
        assertThat(order.state().name()).isEqualTo("CANCELLED");
    }

    @Test
    void aShippedOrderCannotBeCancelled() {
        Order order = new Order("ORD-1");
        order.handle(PAY);
        order.handle(SHIP);

        TransitionResult refused = order.handle(CANCEL);

        assertThat(refused.accepted()).isFalse();
        assertThat(refused.reason()).contains("cannot CANCEL").contains("SHIPPED");
    }

    @Test
    void aRefusedTransitionLeavesTheStateExactlyAsItWas() {
        Order order = new Order("ORD-1");
        order.handle(PAY);
        OrderState before = order.state();
        List<String> historyBefore = order.history();

        TransitionResult refused = order.handle(DELIVER);

        assertThat(refused.accepted()).isFalse();
        assertThat(refused.from()).isEqualTo(refused.to());
        assertThat(order.state()).isSameAs(before);
        assertThat(order.history()).isEqualTo(historyBefore);
    }

    @Test
    void skippingAStepIsRefused() {
        Order order = new Order("ORD-1");

        assertThat(order.handle(SHIP).accepted()).isFalse();
        assertThat(order.handle(DELIVER).accepted()).isFalse();
        assertThat(order.state().name()).isEqualTo("NEW");
    }

    @Test
    void terminalStatesAcceptNothing() {
        for (OrderState terminal : List.of(DeliveredState.INSTANCE, CancelledState.INSTANCE)) {
            assertThat(terminal.terminal()).isTrue();
            for (OrderEventType event : OrderEventType.values()) {
                assertThat(terminal.on(event).accepted())
                        .as("%s should refuse %s", terminal.name(), event)
                        .isFalse();
            }
        }
    }

    @Test
    void theStateSetIsClosed() {
        assertThat(OrderState.class.isSealed()).isTrue();
        assertThat(OrderState.class.getPermittedSubclasses()).hasSize(5);
    }

    @Test
    void statesAreStatelessSingletons() {
        assertThat(NewState.INSTANCE).isSameAs(NewState.INSTANCE);
        Order first = new Order("A");
        Order second = new Order("B");

        assertThat(first.state()).isSameAs(second.state());
    }
}
