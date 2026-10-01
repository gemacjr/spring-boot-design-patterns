package com.example.designpatterns.creational.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <b>Singleton</b> (the classic, container-free form).
 *
 * <p>Uses the <em>initialization-on-demand holder</em> idiom: the nested
 * {@link Holder} class is not loaded until {@link #getInstance()} first runs, and
 * the JVM guarantees class initialization is thread-safe. That gives lazy
 * creation with zero synchronization on the hot path &mdash; no double-checked
 * locking, no {@code volatile}, no lock contention.
 *
 * <p>Full documentation: {@code docs/patterns/singleton.md}
 */
public final class RequestCounter {

    private final AtomicLong counter = new AtomicLong();

    /** Private, so no caller can create a second instance. */
    private RequestCounter() {
    }

    /** Holder for lazy, thread-safe initialization. Loaded on first access only. */
    private static final class Holder {
        private static final RequestCounter INSTANCE = new RequestCounter();
    }

    public static RequestCounter getInstance() {
        return Holder.INSTANCE;
    }

    public long increment() {
        return counter.incrementAndGet();
    }

    public long count() {
        return counter.get();
    }
}
