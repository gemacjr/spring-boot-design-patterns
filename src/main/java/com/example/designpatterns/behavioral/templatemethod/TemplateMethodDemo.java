package com.example.designpatterns.behavioral.templatemethod;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Template Method pattern. */
@Component
public class TemplateMethodDemo implements PatternDemo {

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

    @Override public String id() { return "templatemethod"; }
    @Override public Category category() { return Category.BEHAVIORAL; }
    @Override public String title() { return "Template Method"; }

    @Override
    public String intent() {
        return "Define the skeleton of an algorithm in an operation, deferring some steps to "
                + "subclasses. Subclasses redefine certain steps without changing the "
                + "algorithm's structure.";
    }

    @Override
    public Object run() {
        CsvImportJob csv = new CsvImportJob();
        ImportReport csvReport = csv.run(CSV);
        ImportReport jsonReport = new JsonImportJob().run(JSON);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("csv", csvReport);
        result.put("json", jsonReport);
        result.put("csvSkippedRowsSeenByHook", csv.skippedRows());
        result.put("stepsAreIdentical", csvReport.steps().equals(jsonReport.steps()));
        result.put("note", "Two formats, one algorithm. The steps list is identical because run() "
                + "is final — subclasses fill in steps, they never reorder them.");
        return result;
    }
}
