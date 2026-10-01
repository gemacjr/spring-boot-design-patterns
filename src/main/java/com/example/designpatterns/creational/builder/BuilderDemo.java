package com.example.designpatterns.creational.builder;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Builder pattern. */
@Component
public class BuilderDemo implements PatternDemo {

    @Override public String id() { return "builder"; }
    @Override public Category category() { return Category.CREATIONAL; }
    @Override public String title() { return "Builder"; }

    @Override
    public String intent() {
        return "Separate the construction of a complex object from its representation, "
                + "so the same construction process can create different representations.";
    }

    @Override
    public Object run() {
        OrderRequest order = OrderRequest.builder()
                .customer("Ada Lovelace")
                .addLine("WIDGET-1", 3, new BigDecimal("19.99"))
                .addLine("GADGET-7", 1, new BigDecimal("249.00"))
                .shipTo("12 Analytical Way, London")
                .expedited(true)
                .build();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("order", order.toString());
        result.put("total", order.total());
        result.put("rejectedExample", rejectionReason());
        result.put("note", "build() is the only way to obtain an OrderRequest, and it refuses "
                + "to produce an invalid one. No downstream code needs to re-check.");
        return result;
    }

    private String rejectionReason() {
        try {
            OrderRequest.builder().customer("Ada").shipTo("London").build();
            return "unexpectedly built an order with no lines";
        } catch (IllegalStateException expected) {
            return "building an order with no lines threw: " + expected.getMessage();
        }
    }
}
