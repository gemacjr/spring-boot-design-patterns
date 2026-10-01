package com.example.designpatterns.behavioral.chainofresponsibility;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Concrete Handler</b>: allows a fixed number of submissions.
 *
 * <p>The only stateful handler, which makes it the one worth thinking hardest
 * about: because the chain runs every handler, this counter increments even for
 * input that other handlers have already rejected. Whether that is correct is a
 * product decision — and precisely the kind of consequence the short-circuit
 * policy determines.
 */
@Component
public class RateLimitHandler implements ValidationHandler {

    private static final int LIMIT = 3;

    private final AtomicInteger submissions = new AtomicInteger();

    @Override public int order() { return 40; }

    @Override public String name() { return "rateLimit"; }

    @Override
    public Optional<String> validate(String input) {
        return submissions.incrementAndGet() > LIMIT
                ? Optional.of("rate limit exceeded: at most " + LIMIT + " submissions")
                : Optional.empty();
    }

    public void reset() {
        submissions.set(0);
    }
}
