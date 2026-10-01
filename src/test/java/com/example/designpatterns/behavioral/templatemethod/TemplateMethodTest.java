package com.example.designpatterns.behavioral.templatemethod;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Template Method buys: the algorithm's <em>shape</em> is
 * fixed in one place, and subclasses can vary the steps but not the order.
 */
class TemplateMethodTest {

    private static final String CSV = """
            name,email
            Ada,ada@example.com
            Broken,not-an-email
            Grace,grace@example.com
            """;

    private static final String JSON = """
            [{"name":"Ada","email":"ada@example.com"},
             {"name":"Broken","email":"nope"},
             {"name":"Grace","email":"grace@example.com"}]
            """;

    @Test
    void bothSubclassesExecuteTheIdenticalStepSequence() {
        ImportReport csv = new CsvImportJob().run(CSV);
        ImportReport json = new JsonImportJob().run(JSON);

        assertThat(csv.steps()).containsExactly("begin", "parse", "validate", "persist", "summarise");
        assertThat(json.steps()).isEqualTo(csv.steps());
    }

    @Test
    void eachSubclassStillParsesItsOwnFormat() {
        ImportReport csv = new CsvImportJob().run(CSV);
        ImportReport json = new JsonImportJob().run(JSON);

        assertThat(csv.format()).isEqualTo("CSV");
        assertThat(json.format()).isEqualTo("JSON");
        assertThat(csv.parsed()).isEqualTo(3);
        assertThat(json.parsed()).isEqualTo(3);
        assertThat(csv.imported()).isEqualTo(2);
        assertThat(json.imported()).isEqualTo(2);
    }

    @Test
    void theOptionalHookFiresOnlyForSkippedRows() {
        CsvImportJob job = new CsvImportJob();

        ImportReport report = job.run(CSV);

        assertThat(report.skipped()).isEqualTo(1);
        assertThat(job.skippedRows()).hasSize(1);
        assertThat(job.skippedRows().get(0)).containsEntry("name", "Broken");
    }

    @Test
    void aSubclassCannotReorderTheAlgorithm() throws Exception {
        Method template = DataImportJob.class.getMethod("run", String.class);

        assertThat(Modifier.isFinal(template.getModifiers())).isTrue();
    }
}
