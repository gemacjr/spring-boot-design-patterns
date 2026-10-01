package com.example.designpatterns.behavioral.observer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantees Observer buys: the subject stays ignorant of who is
 * listening, subscriptions are dynamic, and one broken observer cannot silence
 * the rest.
 */
class ObserverTest {

    @Test
    void everySubscriberIsNotified() {
        AuditListener audit = new AuditListener();
        EmailListener email = new EmailListener();
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe(audit);
        publisher.subscribe(email);

        int notified = publisher.publish(OrderEvent.of("ORD-1", "PLACED"));

        assertThat(notified).isEqualTo(2);
        assertThat(audit.received()).hasSize(1);
        assertThat(email.received()).hasSize(1);
    }

    @Test
    void unsubscribingStopsDelivery() {
        AuditListener audit = new AuditListener();
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.subscribe(audit);

        publisher.publish(OrderEvent.of("ORD-1", "PLACED"));
        publisher.unsubscribe(audit);
        publisher.publish(OrderEvent.of("ORD-1", "SHIPPED"));

        assertThat(audit.received()).hasSize(1);
        assertThat(publisher.listenerCount()).isZero();
    }

    @Test
    void oneFailingObserverDoesNotStarveTheOthers() {
        OrderEventPublisher publisher = new OrderEventPublisher();
        AuditListener downstream = new AuditListener();
        publisher.subscribe(event -> {
            throw new IllegalStateException("boom");
        });
        publisher.subscribe(downstream);

        publisher.publish(OrderEvent.of("ORD-1", "PLACED"));

        assertThat(downstream.received()).hasSize(1);
        assertThat(publisher.failures()).hasSize(1);
        assertThat(publisher.failures().get(0)).contains("boom");
    }

    @Test
    void theSubjectNeverNamesAConcreteObserver() {
        boolean namesAConcreteListener = java.util.Arrays.stream(OrderEventPublisher.class.getMethods())
                .flatMap(method -> java.util.Arrays.stream(method.getParameterTypes()))
                .anyMatch(type -> type.getSimpleName().endsWith("Listener")
                        && !type.equals(OrderEventListener.class));

        assertThat(namesAConcreteListener).isFalse();
    }
}
