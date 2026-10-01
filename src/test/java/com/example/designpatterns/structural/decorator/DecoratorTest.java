package com.example.designpatterns.structural.decorator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Asserts the guarantees Decorator buys: behaviour composes at runtime, wrapping
 * order is observable and meaningful, and each layer adds exactly one concern.
 */
class DecoratorTest {

    /** A delegate that fails a fixed number of times before succeeding. */
    private static class FlakySender implements MessageSender {
        private final int failuresBeforeSuccess;
        final AtomicInteger attempts = new AtomicInteger();

        FlakySender(int failuresBeforeSuccess) {
            this.failuresBeforeSuccess = failuresBeforeSuccess;
        }

        @Override
        public String send(String message) {
            if (attempts.incrementAndGet() <= failuresBeforeSuccess) {
                throw new IllegalStateException("transient network failure");
            }
            return "sent:" + message;
        }
    }

    @Test
    void wrappingOrderChangesWhatEachLayerSees() {
        List<String> logOutside = new ArrayList<>();
        List<String> logInside = new ArrayList<>();

        // log wraps encrypt: the logger sees the original plaintext
        new LoggingSender(new EncryptingSender(new PlainMessageSender()), logOutside).send("attack at dawn");
        // encrypt wraps log: the logger sees ciphertext
        new EncryptingSender(new LoggingSender(new PlainMessageSender(), logInside)).send("attack at dawn");

        assertThat(logOutside).anyMatch(line -> line.contains("attack at dawn"));
        assertThat(logInside).noneMatch(line -> line.contains("attack at dawn"));
    }

    @Test
    void encryptionRoundTrips() {
        String cipher = EncryptingSender.encrypt("attack at dawn");

        assertThat(cipher).isNotEqualTo("attack at dawn");
        assertThat(EncryptingSender.encrypt(cipher)).isEqualTo("attack at dawn");
    }

    @Test
    void retryingSucceedsWhenAttemptsSuffice() {
        FlakySender flaky = new FlakySender(2);

        String result = new RetryingSender(flaky, 3).send("hello");

        assertThat(result).isEqualTo("sent:hello");
        assertThat(flaky.attempts.get()).isEqualTo(3);
    }

    @Test
    void retryingGivesUpWhenAttemptsAreExhausted() {
        FlakySender flaky = new FlakySender(2);

        assertThatThrownBy(() -> new RetryingSender(flaky, 2).send("hello"))
                .isInstanceOf(IllegalStateException.class);
        assertThat(flaky.attempts.get()).isEqualTo(2);
    }

    @Test
    void eachDecoratorDelegatesExactlyOncePerSuccessfulCall() {
        AtomicInteger calls = new AtomicInteger();
        MessageSender counting = message -> {
            calls.incrementAndGet();
            return "sent:" + message;
        };

        new LoggingSender(new EncryptingSender(new RetryingSender(counting, 3)), new ArrayList<>())
                .send("hello");

        assertThat(calls.get()).isEqualTo(1);
    }

    @Test
    void aDecoratorIsSubstitutableForThePlainComponent() {
        MessageSender plain = new PlainMessageSender();
        MessageSender decorated = new LoggingSender(plain, new ArrayList<>());

        assertThat(decorated).isInstanceOf(MessageSender.class);
        assertThat(decorated.send("x")).isEqualTo(plain.send("x"));
    }
}
