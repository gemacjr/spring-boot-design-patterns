package com.example.designpatterns.spring.dependencyinjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Two beans of the same type, deliberately.
 *
 * <p>This is the situation that makes {@code @Qualifier} necessary: with two
 * {@link Clock} beans, an unqualified {@code Clock} injection point is ambiguous
 * and the context fails to start. Spring refuses to guess — which is the right
 * behaviour, because guessing wrong here would be silent.
 *
 * <p>{@code @Bean} methods are Factory Methods that the container calls. The method
 * name becomes the bean name, which is what {@code @Qualifier} matches on.
 */
@Configuration
public class ClockConfiguration {

    /** Frozen, so anything depending on it is deterministic in tests. */
    @Bean
    public Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-09-08T10:00:00Z"), ZoneOffset.UTC);
    }

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }
}
