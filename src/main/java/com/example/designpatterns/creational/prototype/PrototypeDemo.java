package com.example.designpatterns.creational.prototype;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Prototype pattern. */
@Component
public class PrototypeDemo implements PatternDemo {

    @Override public String id() { return "prototype"; }
    @Override public Category category() { return Category.CREATIONAL; }
    @Override public String title() { return "Prototype"; }

    @Override
    public String intent() {
        return "Specify the kinds of objects to create using a prototypical instance, "
                + "and create new objects by copying this prototype.";
    }

    @Override
    public Object run() {
        TemplateRegistry registry = new TemplateRegistry();
        ReportTemplate master = new ReportTemplate("Quarterly", "A4", new Watermark("DRAFT", 30));
        registry.register("quarterly", master);

        ReportTemplate mine = registry.newInstance("quarterly");
        mine.watermark().setText("FINAL");
        mine.watermark().setOpacity(100);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("registeredPrototype", master.toString());
        result.put("myMutatedCopy", mine.toString());
        result.put("prototypeUnchanged", "DRAFT".equals(master.watermark().text()));
        result.put("note", "The copy's watermark was mutated to FINAL, yet the registered "
                + "prototype still reads DRAFT. A shallow copy would have changed both.");
        return result;
    }
}
