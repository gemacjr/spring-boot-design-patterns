package com.example.designpatterns.creational.singleton;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Singleton pattern. */
@Component
public class SingletonDemo implements PatternDemo {

    private final CounterBean counterBean;

    public SingletonDemo(CounterBean counterBean) {
        this.counterBean = counterBean;
    }

    @Override public String id() { return "singleton"; }
    @Override public Category category() { return Category.CREATIONAL; }
    @Override public String title() { return "Singleton"; }

    @Override
    public String intent() {
        return "Ensure a class has only one instance, and provide a global point of access to it.";
    }

    @Override
    public Object run() {
        RequestCounter classic = RequestCounter.getInstance();
        classic.increment();
        counterBean.increment();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("classicSingletonCount", classic.count());
        result.put("classicSingletonIdentity", System.identityHashCode(classic));
        result.put("classicSameInstance", classic == RequestCounter.getInstance());
        result.put("springBeanCount", counterBean.count());
        result.put("springBeanIdentity", System.identityHashCode(counterBean));
        result.put("note", "Both counts rise on every call: singletons carry state for the whole "
                + "process lifetime. The Spring bean is one-per-context, not one-per-JVM.");
        return result;
    }
}
