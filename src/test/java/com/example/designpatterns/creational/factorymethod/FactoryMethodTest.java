package com.example.designpatterns.creational.factorymethod;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Asserts the guarantee Factory Method buys: the algorithm around creation is
 * written once in the creator, while subclasses decide which product appears.
 */
class FactoryMethodTest {

    @Test
    void oneInheritedAlgorithmProducesChannelSpecificOutput() {
        String email = new EmailDispatcher().dispatch("ada@example.com", "hello");
        String sms = new SmsDispatcher().dispatch("+15550100", "hello");

        assertThat(email).contains("EMAIL").contains("ada@example.com").contains("hello");
        assertThat(sms).contains("SMS").contains("+15550100").contains("hello");
        assertThat(email).isNotEqualTo(sms);
    }

    @Test
    void theSharedValidationLivesInTheCreatorNotInEachProduct() {
        assertThatThrownBy(() -> new EmailDispatcher().dispatch("  ", "hello"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("recipient");

        assertThatThrownBy(() -> new SmsDispatcher().dispatch("  ", "hello"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("recipient");
    }

    @Test
    void addingACreatorRequiresNoChangeToTheBaseClass() {
        NotificationDispatcher push = new NotificationDispatcher() {
            @Override
            protected NotificationChannel createChannel() {
                return (to, message) -> "PUSH to " + to + ": " + message;
            }
        };

        assertThat(push.dispatch("device-1", "hi")).contains("PUSH to device-1");
    }
}
