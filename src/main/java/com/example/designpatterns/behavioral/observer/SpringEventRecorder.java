package com.example.designpatterns.behavioral.observer;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Spring-managed observer. No registration code anywhere — the annotation is
 * the subscription.
 */
@Component
public class SpringEventRecorder {

    private final List<OrderEvent> seen = new CopyOnWriteArrayList<>();

    @EventListener
    public void on(OrderEvent event) {
        seen.add(event);
    }

    public List<OrderEvent> seen() {
        return List.copyOf(seen);
    }

    public void clear() {
        seen.clear();
    }
}
