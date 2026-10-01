package com.example.designpatterns.behavioral.chainofresponsibility;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** <b>Concrete Handler</b>: rejects a small blocklist. */
@Component
public class ProfanityHandler implements ValidationHandler {

    private static final List<String> BLOCKED = List.of("badword", "spamword");

    @Override public int order() { return 30; }

    @Override public String name() { return "profanity"; }

    @Override
    public Optional<String> validate(String input) {
        if (input == null) {
            return Optional.empty();
        }
        String lower = input.toLowerCase(Locale.ROOT);
        return BLOCKED.stream()
                .filter(lower::contains)
                .findFirst()
                .map(word -> "comment contains blocked word '" + word + "'");
    }
}
