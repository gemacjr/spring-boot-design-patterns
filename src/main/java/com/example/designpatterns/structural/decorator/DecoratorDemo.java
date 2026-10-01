package com.example.designpatterns.structural.decorator;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Catalog entry for the Decorator pattern. */
@Component
public class DecoratorDemo implements PatternDemo {

    @Override public String id() { return "decorator"; }
    @Override public Category category() { return Category.STRUCTURAL; }
    @Override public String title() { return "Decorator"; }

    @Override
    public String intent() {
        return "Attach additional responsibilities to an object dynamically. Decorators "
                + "provide a flexible alternative to subclassing for extending functionality.";
    }

    @Override
    public Object run() {
        List<String> logOutside = new ArrayList<>();
        List<String> logInside = new ArrayList<>();

        MessageSender logThenEncrypt =
                new LoggingSender(new EncryptingSender(new PlainMessageSender()), logOutside);
        MessageSender encryptThenLog =
                new EncryptingSender(new LoggingSender(new PlainMessageSender(), logInside));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("logOutsideEncryption", Map.of(
                "returned", logThenEncrypt.send("attack at dawn"),
                "log", List.copyOf(logOutside)));
        result.put("logInsideEncryption", Map.of(
                "returned", encryptThenLog.send("attack at dawn"),
                "log", List.copyOf(logInside)));
        result.put("note", "Identical layers, opposite order, different security properties: "
                + "one logs plaintext, the other logs ciphertext. Order is part of the design.");
        return result;
    }
}
