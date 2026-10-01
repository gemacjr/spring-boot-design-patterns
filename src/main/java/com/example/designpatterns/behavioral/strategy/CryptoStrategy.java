package com.example.designpatterns.behavioral.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/** <b>Concrete Strategy</b>: on-chain settlement, flat 1%. */
@Component
public class CryptoStrategy implements PaymentStrategy {

    @Override public String key() { return "crypto"; }

    @Override
    public PaymentResult pay(BigDecimal amount) {
        BigDecimal fee = amount.multiply(new BigDecimal("0.01"))
                .setScale(2, RoundingMode.HALF_UP);
        return new PaymentResult(key(), amount, fee,
                "chain-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
