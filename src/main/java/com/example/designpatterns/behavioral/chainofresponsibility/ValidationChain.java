package com.example.designpatterns.behavioral.chainofresponsibility;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <b>Chain</b>: assembles the handlers and walks them in order.
 *
 * <p><b>Policy: collect every failure.</b> The chain runs all handlers and returns
 * all reasons, rather than stopping at the first rejection.
 *
 * <p>Why: this validates user-submitted text. Reporting one problem per round trip
 * turns form filling into whack-a-mole; reporting all of them lets the user fix
 * everything in one pass.
 *
 * <p>The cost, stated honestly: later handlers run on input an earlier handler has
 * already rejected, so every handler must tolerate garbage (see
 * {@link LengthHandler}), and stateful handlers such as {@link RateLimitHandler}
 * count submissions that were never going to succeed. Short-circuiting would avoid
 * both, at the price of the user experience above.
 *
 * <p>Full documentation: {@code docs/patterns/chainofresponsibility.md}
 */
@Service
public class ValidationChain {

    private final List<ValidationHandler> handlers;

    public ValidationChain(List<ValidationHandler> handlers) {
        this.handlers = handlers.stream()
                .sorted(Comparator.comparingInt(ValidationHandler::order))
                .toList();
    }

    public ValidationResult validate(String input) {
        List<String> failures = new ArrayList<>();
        List<String> handlersRun = new ArrayList<>();

        for (ValidationHandler handler : handlers) {
            handlersRun.add(handler.name());
            handler.validate(input).ifPresent(failures::add);
        }

        return new ValidationResult(failures.isEmpty(), failures, handlersRun);
    }

    public List<String> handlerNames() {
        return handlers.stream().map(ValidationHandler::name).toList();
    }
}
