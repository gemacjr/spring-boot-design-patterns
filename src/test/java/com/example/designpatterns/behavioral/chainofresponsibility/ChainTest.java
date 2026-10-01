package com.example.designpatterns.behavioral.chainofresponsibility;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantees this chain buys: order is declared not accidental, every
 * handler is independent, and the collect-all policy is honoured.
 */
class ChainTest {

    private ValidationChain freshChain() {
        return new ValidationChain(List.of(
                new RateLimitHandler(), new ProfanityHandler(),
                new LengthHandler(), new NotBlankHandler()));
    }

    @Test
    void handlersRunInDeclaredOrderRegardlessOfInjectionOrder() {
        // Supplied deliberately backwards.
        assertThat(freshChain().handlerNames())
                .containsExactly("notBlank", "length", "profanity", "rateLimit");
    }

    @Test
    void validInputPassesEveryHandler() {
        ValidationResult result = freshChain().validate("A perfectly reasonable comment.");

        assertThat(result.valid()).isTrue();
        assertThat(result.failures()).isEmpty();
        assertThat(result.handlersRun())
                .containsExactly("notBlank", "length", "profanity", "rateLimit");
    }

    @Test
    void thePolicyIsCollectAllSoEveryHandlerStillRunsAfterAFailure() {
        ValidationResult result = freshChain().validate("  ");

        assertThat(result.valid()).isFalse();
        assertThat(result.failures()).hasSize(2);
        assertThat(result.failures().get(0)).contains("must not be blank");
        assertThat(result.failures().get(1)).contains("at least 3 characters");
        // The decisive assertion: nothing short-circuited.
        assertThat(result.handlersRun())
                .containsExactly("notBlank", "length", "profanity", "rateLimit");
    }

    @Test
    void failuresAreReportedInHandlerOrder() {
        ValidationResult result = freshChain().validate("badword");

        assertThat(result.failures()).hasSize(1);
        assertThat(result.failures().get(0)).contains("blocked word");
    }

    @Test
    void theStatefulHandlerEventuallyRejects() {
        ValidationChain chain = freshChain();
        String good = "A perfectly reasonable comment.";

        assertThat(chain.validate(good).valid()).isTrue();
        assertThat(chain.validate(good).valid()).isTrue();
        assertThat(chain.validate(good).valid()).isTrue();

        ValidationResult fourth = chain.validate(good);
        assertThat(fourth.valid()).isFalse();
        assertThat(fourth.failures().get(0)).contains("rate limit");
    }

    @Test
    void addingAHandlerRequiresNoChangeToTheChainOrToExistingHandlers() {
        ValidationHandler noShouting = new ValidationHandler() {
            @Override public int order() { return 25; }
            @Override public String name() { return "noShouting"; }
            @Override public Optional<String> validate(String input) {
                return input != null && input.equals(input.toUpperCase())
                        ? Optional.of("please do not shout")
                        : Optional.empty();
            }
        };

        ValidationChain chain = new ValidationChain(List.of(
                new NotBlankHandler(), new LengthHandler(), noShouting));

        ValidationResult result = chain.validate("STOP RIGHT THERE");

        assertThat(chain.handlerNames()).containsExactly("notBlank", "length", "noShouting");
        assertThat(result.failures()).contains("please do not shout");
    }
}
