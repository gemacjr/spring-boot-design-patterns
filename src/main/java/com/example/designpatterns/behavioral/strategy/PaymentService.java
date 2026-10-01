package com.example.designpatterns.behavioral.strategy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <b>Context</b>: selects a strategy at runtime and delegates to it.
 *
 * <p>Spring injects every {@link PaymentStrategy} bean as a {@code List}, and the
 * constructor indexes them by their own {@link PaymentStrategy#key()}. Nothing in
 * this class names a concrete strategy, so a new payment rail is one new
 * {@code @Component} and zero edits here.
 *
 * <p>Spring can also inject {@code Map<String, PaymentStrategy>} directly, keyed
 * by <em>bean name</em>. Indexing by {@code key()} instead is deliberate: it keeps
 * the public API stable if a class is ever renamed.
 *
 * <p>Full documentation: {@code docs/patterns/strategy.md}
 */
@Service
public class PaymentService {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentService(List<PaymentStrategy> availableStrategies) {
        this.strategies = availableStrategies.stream()
                .collect(Collectors.toMap(PaymentStrategy::key, Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(
                                    "Two strategies claim the key '" + a.key() + "'");
                        },
                        LinkedHashMap::new));
    }

    /**
     * Selects the strategy for {@code key} and delegates.
     *
     * <p><b>Fallback policy: fail fast.</b> An unrecognised key is a configuration
     * or programming error, not a business outcome, so it throws rather than
     * silently routing the payment somewhere unintended. A default-strategy
     * fallback would let a typo such as {@code "papyal"} charge a card and report
     * success — a failure invisible until the finance report.
     *
     * @throws IllegalArgumentException if no strategy claims that key
     */
    public PaymentResult pay(String key, BigDecimal amount) {
        PaymentStrategy strategy = strategies.get(key);
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unknown payment method '" + key + "'. Available: " + strategies.keySet());
        }
        return strategy.pay(amount);
    }

    public Set<String> availableMethods() {
        return strategies.keySet();
    }
}
