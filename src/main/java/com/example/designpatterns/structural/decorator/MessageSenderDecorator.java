package com.example.designpatterns.structural.decorator;

/**
 * <b>Decorator</b> base class: holds the wrapped component and implements the
 * same interface.
 *
 * <p>Subclasses add exactly one concern each and delegate the rest. Keeping the
 * delegate {@code protected final} means no decorator can rebind its own delegate
 * halfway through a call, which would make a chain impossible to reason about.
 *
 * <p>Full documentation: {@code docs/patterns/decorator.md}
 */
public abstract class MessageSenderDecorator implements MessageSender {

    protected final MessageSender delegate;

    protected MessageSenderDecorator(MessageSender delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("a decorator needs something to decorate");
        }
        this.delegate = delegate;
    }
}
