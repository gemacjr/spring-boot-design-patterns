package com.example.designpatterns.creational.prototype;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>Prototype Registry</b>: keeps configured prototypes and hands out copies.
 *
 * <p>Callers never receive the stored prototype itself, only an independent copy.
 * That invariant is the registry's whole reason to exist.
 *
 * <p>Full documentation: {@code docs/patterns/prototype.md}
 */
public class TemplateRegistry {

    private final Map<String, ReportTemplate> prototypes = new LinkedHashMap<>();

    public void register(String name, ReportTemplate prototype) {
        prototypes.put(name, prototype);
    }

    /**
     * @return a fresh copy, never the stored prototype
     * @throws IllegalArgumentException if no prototype is registered under that name
     */
    public ReportTemplate newInstance(String name) {
        ReportTemplate prototype = prototypes.get(name);
        if (prototype == null) {
            throw new IllegalArgumentException(
                    "No template registered as '" + name + "'. Known: " + prototypes.keySet());
        }
        return prototype.copy();
    }

    public Set<String> names() {
        return prototypes.keySet();
    }
}
