package com.example.designpatterns.structural.decorator;

/**
 * <b>Component</b>: the type both the plain implementation and every decorator share.
 *
 * <p>Because decorators implement this same interface, a decorated sender is
 * substitutable everywhere a plain one is accepted. That substitutability is what
 * makes the layers composable in any order at runtime.
 *
 * <p>Full documentation: {@code docs/patterns/decorator.md}
 */
@FunctionalInterface
public interface MessageSender {

    String send(String message);
}
