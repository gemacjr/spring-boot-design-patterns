package com.example.designpatterns.behavioral.observer;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Observer pattern. */
@Component
public class ObserverDemo implements PatternDemo {

    private final SpringOrderEvents springEvents;
    private final SpringEventRecorder recorder;

    public ObserverDemo(SpringOrderEvents springEvents, SpringEventRecorder recorder) {
        this.springEvents = springEvents;
        this.recorder = recorder;
    }

    @Override public String id() { return "observer"; }
    @Override public Category category() { return Category.BEHAVIORAL; }
    @Override public String title() { return "Observer"; }

    @Override
    public String intent() {
        return "Define a one-to-many dependency between objects so that when one object "
                + "changes state, all its dependents are notified and updated automatically.";
    }

    @Override
    public Object run() {
        AuditListener audit = new AuditListener();
        EmailListener email = new EmailListener();
        MetricsListener metrics = new MetricsListener();

        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe(audit);
        publisher.subscribe(email);
        publisher.subscribe(metrics);
        publisher.subscribe(event -> {
            throw new IllegalStateException("this listener is broken");
        });

        int notified = publisher.publish(OrderEvent.of("ORD-1", "PLACED"));

        publisher.unsubscribe(email);
        publisher.publish(OrderEvent.of("ORD-1", "SHIPPED"));

        recorder.clear();
        springEvents.placeOrder("ORD-2");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("listenersNotifiedOnFirstPublish", notified);
        result.put("audit", audit.received());
        result.put("emailUnsubscribedAfterFirstEvent", email.received());
        result.put("metrics", metrics.received());
        result.put("swallowedListenerFailures", publisher.failures());
        result.put("viaSpringApplicationEvents", recorder.seen());
        result.put("note", "One listener threw and the rest still ran. Email stopped receiving "
                + "after unsubscribing. Spring's @EventListener does the same with no wiring.");
        return result;
    }
}
