package com.example.designpatterns.behavioral.strategy;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * <pre>
 * curl -s -X POST 'localhost:8080/api/patterns/strategy/pay?method=paypal&amp;amount=100.00'
 * curl -s -X POST 'localhost:8080/api/patterns/strategy/pay?method=papyal&amp;amount=100.00'   # 400
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/strategy")
public class StrategyController {

    private final PaymentService paymentService;

    public StrategyController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public PaymentResult pay(@RequestParam String method, @RequestParam BigDecimal amount) {
        return paymentService.pay(method, amount);
    }
}
