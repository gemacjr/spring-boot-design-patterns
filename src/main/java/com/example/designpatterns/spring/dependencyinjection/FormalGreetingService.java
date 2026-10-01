package com.example.designpatterns.spring.dependencyinjection;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * The default implementation.
 *
 * <p>{@code @Profile("!casual")} means "register this bean unless the casual
 * profile is active". Profiles select an implementation by environment without
 * any {@code if} in application code.
 */
@Component
@Profile("!casual")
public class FormalGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Good day, " + name + ".";
    }
}
