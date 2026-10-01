package com.example.designpatterns.spring.dependencyinjection;

/**
 * The abstraction a consumer depends on.
 *
 * <p>Dependency Inversion in one line: {@code GreetingFacade} depends on this
 * interface, not on a concrete greeter, so which greeter it gets is a deployment
 * decision rather than a code change.
 *
 * <p>Full documentation: {@code docs/patterns/dependencyinjection.md}
 */
public interface GreetingService {

    String greet(String name);
}
