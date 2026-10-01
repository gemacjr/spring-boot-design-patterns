package com.example.designpatterns.creational.singleton;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The same responsibility as {@link RequestCounter}, expressed the Spring way.
 *
 * <p>The critical difference: this is an <em>ordinary class</em>. It has a public
 * constructor and can be instantiated freely in a unit test. Singleton-ness is a
 * property of the <em>container</em> (one instance per {@code ApplicationContext},
 * the default bean scope), not a property baked into the type.
 *
 * <p>That is why Spring's approach is preferred: you get the one-instance benefit
 * without the global mutable state and untestability of a classic singleton.
 *
 * <p>Full documentation: {@code docs/patterns/singleton.md}
 */
@Component
public class CounterBean {

    private final AtomicLong counter = new AtomicLong();

    public long increment() {
        return counter.incrementAndGet();
    }

    public long count() {
        return counter.get();
    }
}
