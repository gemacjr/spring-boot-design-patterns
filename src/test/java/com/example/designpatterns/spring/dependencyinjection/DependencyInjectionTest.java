package com.example.designpatterns.spring.dependencyinjection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/** The default wiring, plus the property constructor injection actually buys. */
@SpringBootTest
class DependencyInjectionTest {

    @Autowired GreetingFacade facade;

    @Test
    void theDefaultProfileWiresTheFormalImplementation() {
        assertThat(facade.implementationName()).isEqualTo("FormalGreetingService");
        assertThat(facade.greet("Ada")).startsWith("Good day, Ada.");
    }

    @Test
    void theQualifierResolvedTheAmbiguityBetweenTwoClockBeans() {
        assertThat(facade.greet("Ada")).contains("2026-09-08T10:00:00Z");
    }

    @Test
    void constructorInjectionMakesTheClassTestableWithoutAContainer() {
        GreetingFacade handWired = new GreetingFacade(
                name -> "Test greeting for " + name,
                Clock.fixed(Instant.parse("2020-01-01T00:00:00Z"), java.time.ZoneOffset.UTC));

        assertThat(handWired.greet("Ada"))
                .isEqualTo("Test greeting for Ada (at 2020-01-01T00:00:00Z)");
    }

    @Test
    void everyDependencyIsVisibleInTheConstructorSignature() {
        assertThat(GreetingFacade.class.getDeclaredConstructors()).hasSize(1);
        assertThat(GreetingFacade.class.getDeclaredConstructors()[0].getParameterCount()).isEqualTo(2);
        assertThat(GreetingFacade.class.getDeclaredFields())
                .allMatch(field -> java.lang.reflect.Modifier.isFinal(field.getModifiers()));
    }
}
