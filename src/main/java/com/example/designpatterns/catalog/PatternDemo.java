package com.example.designpatterns.catalog;

/**
 * Contract every pattern package implements exactly once.
 *
 * <p>This interface together with {@link PatternCatalog} is itself a worked
 * example: {@code PatternDemo} is a <em>Strategy</em> (the catalog invokes
 * {@link #run()} without knowing any concrete type) and the catalog is a
 * <em>Registry</em> populated by the container. Adding a twentieth pattern
 * requires editing no existing file.
 *
 * @see <a href="../../../../../../../docs/patterns/README.md">docs/patterns/README.md</a>
 */
public interface PatternDemo {

    /** Stable identifier, equal to the lowercase leaf package name, e.g. {@code "strategy"}. */
    String id();

    /** Which family this pattern belongs to. */
    Category category();

    /** Human-readable name, e.g. {@code "Strategy"}. */
    String title();

    /** The one-line Gang of Four intent. */
    String intent();

    /**
     * Runs a canned demonstration of the pattern.
     *
     * @return a JSON-serialisable result; implementations should return something
     *         that makes the pattern's behaviour visible, not merely {@code "ok"}.
     */
    Object run();
}
