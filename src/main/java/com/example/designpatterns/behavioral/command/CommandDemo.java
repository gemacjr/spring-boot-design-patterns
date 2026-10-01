package com.example.designpatterns.behavioral.command;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the Command pattern. */
@Component
public class CommandDemo implements PatternDemo {

    @Override public String id() { return "command"; }
    @Override public Category category() { return Category.BEHAVIORAL; }
    @Override public String title() { return "Command"; }

    @Override
    public String intent() {
        return "Encapsulate a request as an object, thereby letting you parameterize clients "
                + "with different requests, queue or log requests, and support undoable operations.";
    }

    @Override
    public Object run() {
        Cart cart = new Cart();
        CommandInvoker invoker = new CommandInvoker(cart);

        String empty = cart.snapshot();

        invoker.run(new AddItemCommand("WIDGET-1", 3));
        invoker.run(new AddItemCommand("GADGET-7", 1));
        invoker.run(new ApplyDiscountCommand(new BigDecimal("10")));
        invoker.run(new RemoveItemCommand("WIDGET-1", 2));
        String afterCommands = cart.snapshot();

        invoker.undoLast();
        invoker.undoLast();
        invoker.undoLast();
        invoker.undoLast();
        String afterUndoingEverything = cart.snapshot();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("emptyCart", empty);
        result.put("afterFourCommands", afterCommands);
        result.put("afterUndoingEverything", afterUndoingEverything);
        result.put("returnedToStart", empty.equals(afterUndoingEverything));
        result.put("log", invoker.log());
        result.put("note", "Undo is LIFO and exact. Each command captured whatever it needed to "
                + "reverse itself — the invoker knows nothing about carts.");
        return result;
    }
}
