package com.example.designpatterns.behavioral.command;

/**
 * <b>Command</b>: an action turned into an object.
 *
 * <p>Because the action is an object, it can be stored, queued, logged, sent over
 * a wire — and, crucially, reversed. {@link #undo} is what a plain method call
 * cannot offer, and it is the reason undo/redo stacks are built this way.
 *
 * <p>Each implementation must capture whatever state its {@code undo} will need at
 * construction or execution time. A command that cannot restore what it changed is
 * not undoable, however it is annotated.
 *
 * <p>Full documentation: {@code docs/patterns/command.md}
 */
public interface CartCommand {

    String describe();

    void execute(Cart cart);

    void undo(Cart cart);
}
