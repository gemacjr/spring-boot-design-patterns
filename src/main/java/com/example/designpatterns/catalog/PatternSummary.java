package com.example.designpatterns.catalog;

/**
 * Metadata about one pattern, as returned by the catalog endpoints.
 *
 * <p>A record rather than a class: it is an immutable data carrier crossing the
 * web boundary, which is exactly the DTO role demonstrated in
 * {@code com.example.designpatterns.spring.dto}.
 */
public record PatternSummary(
        String id,
        Category category,
        String title,
        String intent,
        String docsUrl,
        String runUrl) {

    /** Derives a summary from a demo. The single construction path, so links never drift. */
    public static PatternSummary of(PatternDemo demo) {
        return new PatternSummary(
                demo.id(),
                demo.category(),
                demo.title(),
                demo.intent(),
                "docs/patterns/" + demo.id() + ".md",
                "/api/patterns/" + demo.id());
    }
}
