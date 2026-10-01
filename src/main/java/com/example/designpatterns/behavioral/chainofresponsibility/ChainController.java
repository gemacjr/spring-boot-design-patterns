package com.example.designpatterns.behavioral.chainofresponsibility;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * curl -s -X POST 'localhost:8080/api/patterns/chainofresponsibility/validate?comment=hello%20there'
 * curl -s -X POST 'localhost:8080/api/patterns/chainofresponsibility/validate?comment=x'
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/chainofresponsibility")
public class ChainController {

    private final ValidationChain chain;

    public ChainController(ValidationChain chain) {
        this.chain = chain;
    }

    @PostMapping("/validate")
    public ValidationResult validate(@RequestParam(required = false) String comment) {
        return chain.validate(comment);
    }
}
