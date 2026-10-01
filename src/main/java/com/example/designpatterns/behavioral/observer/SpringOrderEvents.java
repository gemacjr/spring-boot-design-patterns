package com.example.designpatterns.behavioral.observer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * The same pattern, delegated to Spring.
 *
 * <p>{@link ApplicationEventPublisher} is the subject; any bean method annotated
 * {@code @EventListener} is an observer. Registration is automatic, so there is no
 * subscribe/unsubscribe bookkeeping and no risk of a leaked listener.
 *
 * <p>Full documentation: {@code docs/patterns/observer.md}
 */
@Component
public class SpringOrderEvents {

    private final ApplicationEventPublisher publisher;

    public SpringOrderEvents(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void placeOrder(String orderId) {
        publisher.publishEvent(OrderEvent.of(orderId, "PLACED"));
    }
}
