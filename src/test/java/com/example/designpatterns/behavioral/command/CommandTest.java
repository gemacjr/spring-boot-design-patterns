package com.example.designpatterns.behavioral.command;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Command buys: any sequence of operations can be reversed
 * exactly, and the invoker needs no knowledge of what the operations do.
 */
class CommandTest {

    @Test
    void undoingEveryCommandRestoresTheExactStartingState() {
        Cart cart = new Cart();
        CommandInvoker invoker = new CommandInvoker(cart);
        String before = cart.snapshot();

        invoker.run(new AddItemCommand("WIDGET-1", 3));
        invoker.run(new ApplyDiscountCommand(new BigDecimal("10")));
        invoker.run(new RemoveItemCommand("WIDGET-1", 2));

        assertThat(cart.snapshot()).isNotEqualTo(before);

        invoker.undoLast();
        invoker.undoLast();
        invoker.undoLast();

        assertThat(cart.snapshot()).isEqualTo(before);
    }

    @Test
    void undoIsLastInFirstOut() {
        Cart cart = new Cart();
        CommandInvoker invoker = new CommandInvoker(cart);
        invoker.run(new AddItemCommand("A", 1));
        invoker.run(new AddItemCommand("B", 1));

        assertThat(invoker.undoLast()).contains("add 1 x B");
        assertThat(invoker.undoLast()).contains("add 1 x A");
    }

    @Test
    void undoingAnEmptyHistoryIsNormalNotAnError() {
        CommandInvoker invoker = new CommandInvoker(new Cart());

        assertThat(invoker.undoLast()).isEmpty();
    }

    @Test
    void removingMoreThanIsPresentDoesNotInventStockOnUndo() {
        Cart cart = new Cart();
        CommandInvoker invoker = new CommandInvoker(cart);
        invoker.run(new AddItemCommand("WIDGET-1", 2));

        invoker.run(new RemoveItemCommand("WIDGET-1", 5));
        invoker.undoLast();

        assertThat(cart.items()).containsEntry("WIDGET-1", 2);
    }

    @Test
    void theInvokerNeverNamesTheReceiversOperations() {
        boolean mentionsCartOperations = java.util.Arrays.stream(CommandInvoker.class.getMethods())
                .map(java.lang.reflect.Method::getName)
                .anyMatch(name -> name.equals("add") || name.equals("remove"));

        assertThat(mentionsCartOperations).isFalse();
    }
}
