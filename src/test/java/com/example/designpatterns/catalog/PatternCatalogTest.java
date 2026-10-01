package com.example.designpatterns.catalog;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The catalog is tested with stand-in demos rather than real pattern packages,
 * so this test stays green no matter which patterns exist yet.
 */
class PatternCatalogTest {

    private record FakeDemo(String id, Category category) implements PatternDemo {
        public String title() { return "Fake " + id; }
        public String intent() { return "intent of " + id; }
        public Object run() { return "ran " + id; }
    }

    @Test
    void indexesEveryRegisteredDemoById() {
        PatternCatalog catalog = new PatternCatalog(
                List.of(new FakeDemo("strategy", Category.BEHAVIORAL),
                        new FakeDemo("builder", Category.CREATIONAL)));

        assertThat(catalog.findById("strategy").title()).isEqualTo("Fake strategy");
        assertThat(catalog.summaries()).hasSize(2);
    }

    @Test
    void rejectsDuplicateIdsAtConstruction() {
        assertThatThrownBy(() -> new PatternCatalog(
                List.of(new FakeDemo("strategy", Category.BEHAVIORAL),
                        new FakeDemo("strategy", Category.STRUCTURAL))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("strategy");
    }

    @Test
    void unknownIdReportsTheValidOnes() {
        PatternCatalog catalog = new PatternCatalog(List.of(new FakeDemo("strategy", Category.BEHAVIORAL)));

        assertThatThrownBy(() -> catalog.findById("nope"))
                .isInstanceOf(UnknownPatternException.class)
                .hasMessageContaining("strategy");
    }

    @Test
    void summaryCarriesDocsAndRunLinks() {
        PatternCatalog catalog = new PatternCatalog(List.of(new FakeDemo("strategy", Category.BEHAVIORAL)));

        PatternSummary summary = catalog.summaries().get(0);
        assertThat(summary.docsUrl()).isEqualTo("docs/patterns/strategy.md");
        assertThat(summary.runUrl()).isEqualTo("/api/patterns/strategy");
    }
}
