package com.example.designpatterns.behavioral.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <b>Concrete Observer</b> base: remembers what it was told.
 *
 * <p>Three named subclasses follow. In a real system these would send email,
 * write an audit row, and increment a counter; here they record, so the fan-out
 * is observable.
 */
public abstract class RecordingListener implements OrderEventListener {

    private final List<String> received = new CopyOnWriteArrayList<>();

    protected void record(String line) {
        received.add(line);
    }

    public List<String> received() {
        return List.copyOf(received);
    }

    public void clear() {
        received.clear();
    }
}
