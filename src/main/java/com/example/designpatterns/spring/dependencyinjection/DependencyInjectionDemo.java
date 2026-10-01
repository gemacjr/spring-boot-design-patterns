package com.example.designpatterns.spring.dependencyinjection;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for Dependency Injection / Inversion of Control. */
@Component
public class DependencyInjectionDemo implements PatternDemo {

    private final GreetingFacade facade;
    private final Environment environment;

    public DependencyInjectionDemo(GreetingFacade facade, Environment environment) {
        this.facade = facade;
        this.environment = environment;
    }

    @Override public String id() { return "dependencyinjection"; }
    @Override public Category category() { return Category.SPRING; }
    @Override public String title() { return "Dependency Injection / IoC"; }

    @Override
    public String intent() {
        return "Invert control of dependency creation: an object declares what it needs and "
                + "the container supplies it, rather than constructing or locating it itself.";
    }

    @Override
    public Object run() {
        // The same facade, unit-tested with hand-built collaborators and no container at all.
        GreetingFacade handWired = new GreetingFacade(
                name -> "Hand-wired hello, " + name, Clock.systemUTC());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        result.put("wiredImplementation", facade.implementationName());
        result.put("containerWired", facade.greet("Ada"));
        result.put("handWiredInAUnitTest", handWired.greet("Ada"));
        result.put("note", "Run with -Dspring.profiles.active=casual and wiredImplementation "
                + "becomes CasualGreetingService — no application code changes. Constructor "
                + "injection is what makes the hand-wired line possible.");
        return result;
    }
}
