package com.example.designpatterns.behavioral.command;

/**
 * <b>Concrete Command</b>: removes a quantity.
 *
 * <p>Note it records what was actually removed at execute time. Blindly adding
 * the requested quantity back on undo would invent stock that was never there if
 * the cart held less than the caller asked to remove.
 */
public class RemoveItemCommand implements CartCommand {

    private final String sku;
    private final int quantity;
    private int actuallyRemoved;

    public RemoveItemCommand(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    @Override public String describe() { return "remove " + quantity + " x " + sku; }

    @Override
    public void execute(Cart cart) {
        int before = cart.items().getOrDefault(sku, 0);
        cart.remove(sku, quantity);
        int after = cart.items().getOrDefault(sku, 0);
        actuallyRemoved = before - after;
    }

    @Override
    public void undo(Cart cart) {
        if (actuallyRemoved > 0) {
            cart.add(sku, actuallyRemoved);
        }
    }
}
