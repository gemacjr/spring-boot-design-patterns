package com.example.designpatterns.structural.decorator;

/**
 * <b>Concrete Decorator</b>: adds a retry policy without the component or any
 * other decorator knowing retries exist.
 *
 * <p>This is the decorator that best shows why the pattern beats inheritance:
 * retry, logging and encryption are three independent axes. Expressed as
 * subclasses you would need one class per combination; expressed as decorators
 * you compose them at the call site.
 */
public class RetryingSender extends MessageSenderDecorator {

    private final int maxAttempts;

    public RetryingSender(MessageSender delegate, int maxAttempts) {
        super(delegate);
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        this.maxAttempts = maxAttempts;
    }

    @Override
    public String send(String message) {
        RuntimeException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return delegate.send(message);
            } catch (RuntimeException e) {
                last = e;
            }
        }
        throw last;
    }
}
