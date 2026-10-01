package com.example.designpatterns.behavioral.chainofresponsibility;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Catalog entry for the Chain of Responsibility pattern. */
@Component
public class ChainDemo implements PatternDemo {

    @Override public String id() { return "chainofresponsibility"; }
    @Override public Category category() { return Category.BEHAVIORAL; }
    @Override public String title() { return "Chain of Responsibility"; }

    @Override
    public String intent() {
        return "Avoid coupling the sender of a request to its receiver by giving more than one "
                + "object a chance to handle the request. Chain the receivers and pass the "
                + "request along the chain.";
    }

    @Override
    public Object run() {
        // A fresh chain per run, so the stateful rate limiter starts clean.
        ValidationChain chain = new ValidationChain(List.of(
                new RateLimitHandler(), new ProfanityHandler(),
                new LengthHandler(), new NotBlankHandler()));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("handlerOrder", chain.handlerNames());
        result.put("validComment", chain.validate("A perfectly reasonable comment."));
        result.put("blankComment", chain.validate("  "));
        result.put("shortAndProfane", chain.validate("badword"));
        result.put("note", "Handlers were supplied in reverse order and still ran sorted. The "
                + "blank comment collected two failures at once, because the chain policy is "
                + "collect-all rather than stop-at-first.");
        return result;
    }
}
