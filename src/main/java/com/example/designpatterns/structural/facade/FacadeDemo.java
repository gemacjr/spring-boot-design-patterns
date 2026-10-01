package com.example.designpatterns.structural.facade;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Facade pattern. */
@Component
public class FacadeDemo implements PatternDemo {

    @Override public String id() { return "facade"; }
    @Override public Category category() { return Category.STRUCTURAL; }
    @Override public String title() { return "Facade"; }

    @Override
    public String intent() {
        return "Provide a unified interface to a set of interfaces in a subsystem, defining a "
                + "higher-level interface that makes the subsystem easier to use.";
    }

    @Override
    public Object run() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("happyPath", new CheckoutFacade().checkout("WIDGET-1", 2, "4111111111111111", "London"));
        result.put("paymentDeclined", new CheckoutFacade().checkout("WIDGET-1", 2, "FAIL-CARD", "London"));
        result.put("outOfStock", new CheckoutFacade().checkout("WIDGET-1", 9999, "4111111111111111", "London"));
        result.put("note", "Compare stepsTaken across the three. The failure path releases the "
                + "reservation and never touches shipping — logic no caller has to repeat.");
        return result;
    }
}
