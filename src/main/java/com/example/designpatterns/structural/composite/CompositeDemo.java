package com.example.designpatterns.structural.composite;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Composite pattern. */
@Component
public class CompositeDemo implements PatternDemo {

    @Override public String id() { return "composite"; }
    @Override public Category category() { return Category.STRUCTURAL; }
    @Override public String title() { return "Composite"; }

    @Override
    public String intent() {
        return "Compose objects into tree structures to represent part-whole hierarchies. "
                + "Composite lets clients treat individual objects and compositions uniformly.";
    }

    @Override
    public Object run() {
        OrgNode acme = new Department("Acme")
                .add(new Department("Engineering")
                        .add(new Employee("Ada", new BigDecimal("9000")))
                        .add(new Employee("Grace", new BigDecimal("9500")))
                        .add(new Department("Platform")
                                .add(new Employee("Linus", new BigDecimal("8800")))
                                .add(new Employee("Margaret", new BigDecimal("9200")))))
                .add(new Department("Sales")
                        .add(new Employee("Don", new BigDecimal("7000"))));

        OrgNode oneEmployee = new Employee("Ada", new BigDecimal("9000"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tree", acme.render(0).lines().toList());
        result.put("wholeCompanyHeadcount", acme.headcount());
        result.put("wholeCompanyCost", acme.monthlyCost());
        result.put("singleEmployeeHeadcount", oneEmployee.headcount());
        result.put("singleEmployeeCost", oneEmployee.monthlyCost());
        result.put("note", "The same four methods were called on a five-person company and on one "
                + "employee. No client code branches on which it holds.");
        return result;
    }
}
