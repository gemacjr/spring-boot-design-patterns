package com.example.designpatterns.behavioral.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/** <b>Concrete Strategy</b>: PayPal, 3.4% + 20p. */
@Component
public class PayPalStrategy implements PaymentStrategy {

    @Override public String key() { return "paypal"; }

    @Override
    public PaymentResult pay(BigDecimal amount) {
        BigDecimal fee = amount.multiply(new BigDecimal("0.034"))
                .add(new BigDecimal("0.20"))
                .setScale(2, RoundingMode.HALF_UP);
        return new PaymentResult(key(), amount, fee,
                "pp-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
