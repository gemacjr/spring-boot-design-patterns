package com.example.designpatterns.structural.facade;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Facade buys: one call orchestrates three subsystems in
 * the correct order, and the failure path unwinds correctly — knowledge the
 * caller would otherwise have to carry itself.
 */
class FacadeTest {

    @Test
    void oneCallDrivesEverySubsystemInOrder() {
        CheckoutResult result = new CheckoutFacade().checkout("WIDGET-1", 2, "4111111111111111", "London");

        assertThat(result.success()).isTrue();
        assertThat(result.orderId()).isNotBlank();
        assertThat(result.stepsTaken()).containsExactly(
                "inventory.reserve",
                "payment.charge",
                "shipping.schedule");
    }

    @Test
    void aPaymentFailureStopsShippingAndReleasesTheReservation() {
        CheckoutResult result = new CheckoutFacade().checkout("WIDGET-1", 2, "FAIL-CARD", "London");

        assertThat(result.success()).isFalse();
        assertThat(result.stepsTaken()).containsExactly(
                "inventory.reserve",
                "payment.charge",
                "inventory.release");
        assertThat(result.stepsTaken()).noneMatch(step -> step.startsWith("shipping"));
        assertThat(result.message()).containsIgnoringCase("payment");
    }

    @Test
    void insufficientStockNeverReachesPayment() {
        CheckoutResult result = new CheckoutFacade().checkout("WIDGET-1", 9_999, "4111111111111111", "London");

        assertThat(result.success()).isFalse();
        assertThat(result.stepsTaken()).containsExactly("inventory.reserve");
        assertThat(result.message()).containsIgnoringCase("stock");
    }
}
