package com.example.designpatterns.creational.abstractfactory;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Catalog entry for the Abstract Factory pattern. */
@Component
public class AbstractFactoryDemo implements PatternDemo {

    @Override public String id() { return "abstractfactory"; }
    @Override public Category category() { return Category.CREATIONAL; }
    @Override public String title() { return "Abstract Factory"; }

    @Override
    public String intent() {
        return "Provide an interface for creating families of related or dependent objects "
                + "without specifying their concrete classes.";
    }

    @Override
    public Object run() {
        ReportAssembler assembler = new ReportAssembler();
        List<String> rows = List.of("Widgets: 1,204", "Gadgets: 87", "Sprockets: 33");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("pdf", assembler.assemble(new PdfReportFactory(), "Q3 Sales", rows));
        result.put("html", assembler.assemble(new HtmlReportFactory(), "Q3 Sales", rows));
        result.put("note", "One assembler, two families. The assembler names no concrete part, "
                + "so the parts can never be mixed across families.");
        return result;
    }
}
