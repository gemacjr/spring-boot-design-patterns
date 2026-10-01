package com.example.designpatterns.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Browsable index of every pattern in the showcase.
 *
 * <p>Start here: {@code GET /api/patterns}.
 */
@RestController
@RequestMapping("/api/patterns")
public class CatalogController {

    private final PatternCatalog catalog;

    public CatalogController(PatternCatalog catalog) {
        this.catalog = catalog;
    }

    /** All patterns, grouped by category. */
    @GetMapping
    public Map<Category, List<PatternSummary>> index() {
        return catalog.summaries().stream()
                .collect(Collectors.groupingBy(
                        PatternSummary::category, LinkedHashMap::new, Collectors.toList()));
    }

    /** One pattern's metadata plus the result of running its canned demo. */
    @GetMapping("/{id}")
    public Map<String, Object> run(@PathVariable String id) {
        PatternDemo demo = catalog.findById(id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pattern", PatternSummary.of(demo));
        body.put("result", demo.run());
        return body;
    }
}
