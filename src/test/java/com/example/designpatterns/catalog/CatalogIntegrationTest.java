package com.example.designpatterns.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The whole-application check: every pattern is registered, reachable, and
 * self-describing.
 *
 * <p>This test was written before most of the pattern packages existed and failed
 * until the last one landed — it doubled as the progress bar for the build.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CatalogIntegrationTest {

    private static final List<String> EXPECTED_IDS = List.of(
            "singleton", "factorymethod", "abstractfactory", "builder", "prototype",
            "adapter", "decorator", "proxy", "facade", "composite",
            "strategy", "observer", "templatemethod", "chainofresponsibility", "command", "state",
            "dependencyinjection", "repository", "dto");

    @Autowired MockMvc mockMvc;
    @Autowired PatternCatalog catalog;

    @Test
    void catalogHoldsAllNineteenPatterns() {
        assertThat(catalog.summaries()).extracting(PatternSummary::id)
                .containsExactlyInAnyOrderElementsOf(EXPECTED_IDS);
    }

    @Test
    void everyPatternRunsThroughItsEndpoint() throws Exception {
        for (String id : EXPECTED_IDS) {
            mockMvc.perform(get("/api/patterns/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pattern.id").value(id))
                    .andExpect(jsonPath("$.result").exists());
        }
    }

    @Test
    void theIndexGroupsPatternsByCategory() throws Exception {
        mockMvc.perform(get("/api/patterns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CREATIONAL.length()").value(5))
                .andExpect(jsonPath("$.STRUCTURAL.length()").value(5))
                .andExpect(jsonPath("$.BEHAVIORAL.length()").value(6))
                .andExpect(jsonPath("$.SPRING.length()").value(3));
    }

    @Test
    void unknownPatternReturnsProblemDetailListingValidIds() throws Exception {
        mockMvc.perform(get("/api/patterns/{id}", "does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Unknown pattern"))
                .andExpect(jsonPath("$.knownIds").isArray());
    }

    @Test
    void everyPatternDeclaresIntentAndDocsLink() {
        assertThat(catalog.summaries()).allSatisfy(summary -> {
            assertThat(summary.intent()).isNotBlank();
            assertThat(summary.title()).isNotBlank();
            assertThat(summary.docsUrl()).isEqualTo("docs/patterns/" + summary.id() + ".md");
            assertThat(summary.runUrl()).isEqualTo("/api/patterns/" + summary.id());
        });
    }

    /** Every registered pattern must have its write-up, and every package its stub. */
    @Test
    void everyPatternHasDocumentation() {
        assertThat(catalog.summaries()).allSatisfy(summary ->
                assertThat(Path.of("docs/patterns", summary.id() + ".md"))
                        .as("write-up for '%s'", summary.id())
                        .exists());
    }

    @Test
    void everyPatternPackageHasAReadmeStub() throws Exception {
        Path root = Path.of("src/main/java/com/example/designpatterns");
        try (Stream<Path> dirs = Files.walk(root, 2)) {
            List<Path> leafPackages = dirs
                    .filter(Files::isDirectory)
                    .filter(p -> p.getParent() != null
                            && List.of("creational", "structural", "behavioral", "spring")
                                    .contains(p.getParent().getFileName().toString()))
                    .toList();

            assertThat(leafPackages).hasSize(19);
            assertThat(leafPackages).allSatisfy(pkg ->
                    assertThat(pkg.resolve("README.md"))
                            .as("README stub in %s", pkg)
                            .exists());
        }
    }

    /**
     * The isolation constraint, enforced rather than merely documented: a pattern
     * package may import the catalog contract and the JDK, but never another
     * pattern package. Without this check the packages would quietly grow
     * dependencies on each other and stop being independently readable.
     */
    @Test
    void noPatternPackageImportsAnotherPatternPackage() throws Exception {
        Path root = Path.of("src/main/java/com/example/designpatterns");
        assertThat(root).exists();

        List<String> violations = new java.util.ArrayList<>();
        try (Stream<Path> files = Files.walk(root)) {
            for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                String ownPackage = file.getParent().toString();
                if (ownPackage.endsWith("designpatterns") || ownPackage.contains("catalog")) {
                    continue;
                }
                String ownLeaf = file.getParent().getFileName().toString();
                for (String line : Files.readAllLines(file)) {
                    if (!line.startsWith("import com.example.designpatterns.")) {
                        continue;
                    }
                    boolean isCatalog = line.contains(".catalog.");
                    boolean isOwnPackage = line.contains("." + ownLeaf + ".");
                    if (!isCatalog && !isOwnPackage) {
                        violations.add(file.getFileName() + " -> " + line.trim());
                    }
                }
            }
        }

        assertThat(violations).isEmpty();
    }
}
