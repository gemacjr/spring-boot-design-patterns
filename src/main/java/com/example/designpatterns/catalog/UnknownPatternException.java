package com.example.designpatterns.catalog;

import java.util.Collection;
import java.util.List;

/**
 * Thrown when a caller asks for a pattern id the catalog does not hold.
 *
 * <p>Carries the known ids so the error response can teach rather than merely refuse.
 */
public class UnknownPatternException extends RuntimeException {

    private final List<String> knownIds;

    public UnknownPatternException(String id, Collection<String> knownIds) {
        super("Unknown pattern '" + id + "'. Known ids: " + String.join(", ", knownIds));
        this.knownIds = List.copyOf(knownIds);
    }

    public List<String> knownIds() {
        return knownIds;
    }
}
