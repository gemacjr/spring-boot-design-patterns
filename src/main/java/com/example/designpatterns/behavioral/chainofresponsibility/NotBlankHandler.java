package com.example.designpatterns.behavioral.chainofresponsibility;

import org.springframework.stereotype.Component;

import java.util.Optional;

/** <b>Concrete Handler</b>: rejects null or whitespace-only input. */
@Component
public class NotBlankHandler implements ValidationHandler {

    @Override public int order() { return 10; }

    @Override public String name() { return "notBlank"; }

    @Override
    public Optional<String> validate(String input) {
        return (input == null || input.isBlank())
                ? Optional.of("comment must not be blank")
                : Optional.empty();
    }
}
