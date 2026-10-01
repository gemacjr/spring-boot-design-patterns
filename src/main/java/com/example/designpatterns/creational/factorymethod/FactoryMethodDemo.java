package com.example.designpatterns.creational.factorymethod;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Factory Method pattern. */
@Component
public class FactoryMethodDemo implements PatternDemo {

    @Override public String id() { return "factorymethod"; }
    @Override public Category category() { return Category.CREATIONAL; }
    @Override public String title() { return "Factory Method"; }

    @Override
    public String intent() {
        return "Define an interface for creating an object, but let subclasses decide "
                + "which class to instantiate.";
    }

    @Override
    public Object run() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("viaEmailDispatcher", new EmailDispatcher().dispatch("ada@example.com", "Build finished"));
        result.put("viaSmsDispatcher", new SmsDispatcher().dispatch("+15550100", "Build finished"));
        result.put("note", "Both lines came from the same inherited dispatch() method. Only the "
                + "product differs, because only createChannel() was overridden.");
        return result;
    }
}
