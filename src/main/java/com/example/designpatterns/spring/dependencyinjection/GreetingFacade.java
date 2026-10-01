package com.example.designpatterns.spring.dependencyinjection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * Constructor injection, and why it is the only form worth using.
 *
 * <p>Both dependencies are {@code final}: the object cannot exist in a
 * half-configured state, and nothing can swap them out later. Both are visible in
 * the constructor signature, so the class's true dependencies are impossible to
 * hide — if the parameter list grows uncomfortable, that discomfort is telling you
 * the class does too much.
 *
 * <p>Contrast field injection ({@code @Autowired} on a private field): it needs no
 * constructor, so a unit test cannot supply a dependency without reflection, the
 * fields cannot be {@code final}, and a class with fifteen dependencies looks
 * exactly as innocent as one with two.
 *
 * <p>Full documentation: {@code docs/patterns/dependencyinjection.md}
 */
@Component
public class GreetingFacade {

    private final GreetingService greetingService;
    private final Clock clock;

    public GreetingFacade(GreetingService greetingService, @Qualifier("fixedClock") Clock clock) {
        this.greetingService = greetingService;
        this.clock = clock;
    }

    public String greet(String name) {
        return greetingService.greet(name) + " (at " + clock.instant() + ")";
    }

    /** Exposed so tests can show which implementation was wired in. */
    public String implementationName() {
        return greetingService.getClass().getSimpleName();
    }
}
