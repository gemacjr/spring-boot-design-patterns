package com.example.designpatterns.spring.dependencyinjection;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Registered only when the {@code casual} profile is active. */
@Component
@Profile("casual")
public class CasualGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Hey " + name + "!";
    }
}
