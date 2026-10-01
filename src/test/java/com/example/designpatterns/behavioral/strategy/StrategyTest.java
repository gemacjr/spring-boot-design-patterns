package com.example.designpatterns.behavioral.strategy;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Asserts the guarantee Strategy buys: the set of algorithms is open for
 * extension while the context that selects among them stays closed for
 * modification.
 */
class StrategyTest {

    private PaymentService serviceWith(PaymentStrategy... strategies) {
        return new PaymentService(List.of(strategies));
    }

    @Test
    void eachStrategyAppliesItsOwnFeeSchedule() {
        PaymentService service = serviceWith(
                new CreditCardStrategy(), new PayPalStrategy(), new CryptoStrategy());
        BigDecimal amount = new BigDecimal("100.00");

        assertThat(service.pay("card", amount).fee()).isEqualByComparingTo("3.20");
        assertThat(service.pay("paypal", amount).fee()).isEqualByComparingTo("3.60");
        assertThat(service.pay("crypto", amount).fee()).isEqualByComparingTo("1.00");
    }

    @Test
    void addingAStrategyRequiresNoChangeToTheContextOrToExistingStrategies() {
        PaymentStrategy giftCard = new PaymentStrategy() {
            @Override public String key() { return "giftcard"; }
            @Override public PaymentResult pay(BigDecimal amount) {
                return new PaymentResult(key(), amount, BigDecimal.ZERO, "gc-0001");
            }
        };

        PaymentService service = serviceWith(new CreditCardStrategy(), giftCard);

        assertThat(service.availableMethods()).contains("giftcard");
        assertThat(service.pay("giftcard", new BigDecimal("50.00")).fee())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void anUnknownMethodFailsFastAndSaysWhatIsAvailable() {
        PaymentService service = serviceWith(new CreditCardStrategy(), new PayPalStrategy());

        assertThatThrownBy(() -> service.pay("papyal", BigDecimal.TEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("papyal")
                .hasMessageContaining("card")
                .hasMessageContaining("paypal");
    }

    @Test
    void twoStrategiesClaimingTheSameKeyFailAtWiringTimeNotAtPaymentTime() {
        assertThatThrownBy(() -> serviceWith(new CreditCardStrategy(), new CreditCardStrategy()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("card");
    }

    @Test
    void theContextNeverNamesAConcreteStrategy() {
        List<String> concreteTypesInSignatures = new ArrayList<>();
        for (java.lang.reflect.Method method : PaymentService.class.getDeclaredMethods()) {
            for (Class<?> parameter : method.getParameterTypes()) {
                if (PaymentStrategy.class.isAssignableFrom(parameter)
                        && !parameter.equals(PaymentStrategy.class)) {
                    concreteTypesInSignatures.add(parameter.getSimpleName());
                }
            }
        }

        assertThat(concreteTypesInSignatures).isEmpty();
    }
}
