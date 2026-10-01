package com.example.designpatterns.behavioral.state;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Catalog entry for the State pattern. */
@Component
public class StateDemo implements PatternDemo {

    @Override public String id() { return "state"; }
    @Override public Category category() { return Category.BEHAVIORAL; }
    @Override public String title() { return "State"; }

    @Override
    public String intent() {
        return "Allow an object to alter its behaviour when its internal state changes. "
                + "The object will appear to change its class.";
    }

    @Override
    public Object run() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("happyPath", replay(List.of(
                OrderEventType.PAY, OrderEventType.SHIP, OrderEventType.DELIVER)));
        result.put("cancelledBeforeShipping", replay(List.of(
                OrderEventType.PAY, OrderEventType.CANCEL)));
        result.put("cancelRefusedAfterShipping", replay(List.of(
                OrderEventType.PAY, OrderEventType.SHIP, OrderEventType.CANCEL)));
        result.put("eventsAfterATerminalState", replay(List.of(
                OrderEventType.CANCEL, OrderEventType.PAY, OrderEventType.SHIP)));
        result.put("note", "Refusals are values, not exceptions, and they leave the state "
                + "unchanged. The Order context contains no if or switch over status.");
        return result;
    }

    private Map<String, Object> replay(List<OrderEventType> events) {
        Order order = new Order("ORD-1");
        List<TransitionResult> transitions = events.stream().map(order::handle).toList();

        Map<String, Object> run = new LinkedHashMap<>();
        run.put("events", events);
        run.put("transitions", transitions);
        run.put("finalState", order.state().name());
        run.put("history", order.history());
        return run;
    }
}
