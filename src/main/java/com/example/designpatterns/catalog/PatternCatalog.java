package com.example.designpatterns.catalog;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of every {@link PatternDemo} in the application.
 *
 * <p>Spring injects the complete list of implementations into the constructor.
 * No pattern package is named anywhere in this class, so the catalog is closed
 * for modification and open for extension &mdash; the Open/Closed Principle that
 * most of the patterns in this showcase exist to serve.
 *
 * <p>Duplicate ids fail at context startup rather than silently shadowing one
 * another, because a shadowed pattern would be invisible and very confusing.
 */
@Component
public class PatternCatalog {

    private final Map<String, PatternDemo> byId = new LinkedHashMap<>();

    public PatternCatalog(List<PatternDemo> demos) {
        demos.stream()
                .sorted(Comparator.comparing(PatternDemo::category).thenComparing(PatternDemo::id))
                .forEach(demo -> {
                    PatternDemo previous = byId.putIfAbsent(demo.id(), demo);
                    if (previous != null) {
                        throw new IllegalStateException(
                                "Duplicate pattern id '" + demo.id() + "' declared by "
                                        + previous.getClass().getName() + " and "
                                        + demo.getClass().getName());
                    }
                });
    }

    /**
     * @throws UnknownPatternException if no pattern with that id is registered
     */
    public PatternDemo findById(String id) {
        PatternDemo demo = byId.get(id);
        if (demo == null) {
            throw new UnknownPatternException(id, byId.keySet());
        }
        return demo;
    }

    /** Every registered pattern, ordered by category then id. */
    public List<PatternSummary> summaries() {
        return byId.values().stream().map(PatternSummary::of).toList();
    }
}
