package com.example.designpatterns.behavioral.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <b>Subject</b>: keeps the observer list and fans events out to it.
 *
 * <p>Two decisions here are worth noticing, because naive implementations get
 * them wrong:
 *
 * <ul>
 *   <li><b>A failing observer must not silence the others.</b> Without the
 *       try/catch, one broken listener would swallow the notification for every
 *       listener registered after it.</li>
 *   <li><b>The listener list is copy-on-write.</b> A listener that unsubscribes
 *       itself during {@code publish} would otherwise cause a
 *       {@code ConcurrentModificationException}.</li>
 * </ul>
 *
 * <p>Full documentation: {@code docs/patterns/observer.md}
 */
public class OrderEventPublisher {

    private final List<OrderEventListener> listeners = new CopyOnWriteArrayList<>();
    private final List<String> failures = new CopyOnWriteArrayList<>();

    public void subscribe(OrderEventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(OrderEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return how many listeners were notified, including ones that threw
     */
    public int publish(OrderEvent event) {
        int notified = 0;
        for (OrderEventListener listener : listeners) {
            notified++;
            try {
                listener.onEvent(event);
            } catch (RuntimeException e) {
                failures.add(listener.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
        return notified;
    }

    /** Problems swallowed during publish, so they are visible rather than lost. */
    public List<String> failures() {
        return List.copyOf(failures);
    }

    public int listenerCount() {
        return listeners.size();
    }
}
