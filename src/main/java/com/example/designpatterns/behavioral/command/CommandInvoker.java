package com.example.designpatterns.behavioral.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * <b>Invoker</b>: runs commands and keeps the history that makes undo possible.
 *
 * <p>The invoker knows nothing about carts, discounts or SKUs. It could drive a
 * text editor's commands unchanged. That ignorance is the pattern's payoff.
 *
 * <p>History is a stack, so undo is LIFO. {@link #undoLast()} returns an
 * {@link Optional} rather than throwing on an empty history: "nothing left to
 * undo" is a normal state of a UI, not an error.
 *
 * <p>Full documentation: {@code docs/patterns/command.md}
 */
public class CommandInvoker {

    private final Cart cart;
    private final Deque<CartCommand> history = new ArrayDeque<>();
    private final List<String> log = new ArrayList<>();

    public CommandInvoker(Cart cart) {
        this.cart = cart;
    }

    public void run(CartCommand command) {
        command.execute(cart);
        history.push(command);
        log.add("did: " + command.describe());
    }

    /** @return the description of what was undone, or empty if there was nothing */
    public Optional<String> undoLast() {
        if (history.isEmpty()) {
            return Optional.empty();
        }
        CartCommand command = history.pop();
        command.undo(cart);
        log.add("undid: " + command.describe());
        return Optional.of(command.describe());
    }

    /** Descriptions of the commands still on the undo stack, most recent first. */
    public List<String> history() {
        return history.stream().map(CartCommand::describe).toList();
    }

    public List<String> log() {
        return List.copyOf(log);
    }
}
