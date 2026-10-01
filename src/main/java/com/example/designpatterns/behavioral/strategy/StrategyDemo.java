package com.example.designpatterns.behavioral.strategy;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Strategy pattern. */
@Component
public class StrategyDemo implements PatternDemo {

    private final PaymentService paymentService;

    public StrategyDemo(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override public String id() { return "strategy"; }
    @Override public Category category() { return Category.BEHAVIORAL; }
    @Override public String title() { return "Strategy"; }

    @Override
    public String intent() {
        return "Define a family of algorithms, encapsulate each one, and make them "
                + "interchangeable. Strategy lets the algorithm vary independently of "
                + "the clients that use it.";
    }

    @Override
    public Object run() {
        BigDecimal amount = new BigDecimal("100.00");

        Map<String, Object> quotes = new LinkedHashMap<>();
        for (String method : paymentService.availableMethods()) {
            quotes.put(method, paymentService.pay(method, amount));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("availableMethods", paymentService.availableMethods());
        result.put("sameAmountEachRail", quotes);
        result.put("unknownMethod", unknownMethodMessage());
        result.put("note", "PaymentService names no concrete strategy. Adding a rail means adding "
                + "one @Component; this class and the existing strategies stay untouched.");
        return result;
    }

    private String unknownMethodMessage() {
        try {
            paymentService.pay("papyal", BigDecimal.ONE);
            return "unexpectedly accepted an unknown method";
        } catch (IllegalArgumentException expected) {
            return expected.getMessage();
        }
    }
}
