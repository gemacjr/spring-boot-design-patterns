package com.example.designpatterns.behavioral.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/** <b>Concrete Strategy</b>: card rails, 2.9% + 30p. */
@Component
public class CreditCardStrategy implements PaymentStrategy {

    @Override public String key() { return "card"; }

    @Override
    public PaymentResult pay(BigDecimal amount) {
        BigDecimal fee = amount.multiply(new BigDecimal("0.029"))
                .add(new BigDecimal("0.30"))
                .setScale(2, RoundingMode.HALF_UP);
        return new PaymentResult(key(), amount, fee, "card-" + reference());
    }

    private static String reference() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
