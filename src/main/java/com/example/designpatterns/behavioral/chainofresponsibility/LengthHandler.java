package com.example.designpatterns.behavioral.chainofresponsibility;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <b>Concrete Handler</b>: enforces a length window.
 *
 * <p>Because the chain collects failures rather than stopping at the first, this
 * handler will run on input that {@link NotBlankHandler} has already rejected. It
 * therefore null-checks defensively — a requirement the short-circuiting variant
 * would not have. Policy choices have consequences for every link.
 */
@Component
public class LengthHandler implements ValidationHandler {

    private static final int MIN = 3;
    private static final int MAX = 100;

    @Override public int order() { return 20; }

    @Override public String name() { return "length"; }

    @Override
    public Optional<String> validate(String input) {
        int length = input == null ? 0 : input.strip().length();
        if (length < MIN) {
            return Optional.of("comment must be at least " + MIN + " characters (was " + length + ")");
        }
        if (length > MAX) {
            return Optional.of("comment must be at most " + MAX + " characters (was " + length + ")");
        }
        return Optional.empty();
    }
}
