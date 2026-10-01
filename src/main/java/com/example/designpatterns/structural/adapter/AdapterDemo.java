package com.example.designpatterns.structural.adapter;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Adapter pattern. */
@Component
public class AdapterDemo implements PatternDemo {

    @Override public String id() { return "adapter"; }
    @Override public Category category() { return Category.STRUCTURAL; }
    @Override public String title() { return "Adapter"; }

    @Override
    public String intent() {
        return "Convert the interface of a class into another interface clients expect, "
                + "letting classes work together that could not otherwise because of "
                + "incompatible interfaces.";
    }

    @Override
    public Object run() {
        LegacyXmlShippingApi legacy = new LegacyXmlShippingApi();
        ShippingService adapted = new LegacyShippingAdapter(legacy);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("whatTheLegacyApiReturns", legacy.getRateXml("SW1A 1AA", 2500));
        result.put("whatTheApplicationSees", adapted.quote("SW1A 1AA", 2.5));
        result.put("note", "Same call, two vocabularies. Only LegacyShippingAdapter knows the "
                + "legacy API exists; replacing the vendor means rewriting one class.");
        return result;
    }
}
