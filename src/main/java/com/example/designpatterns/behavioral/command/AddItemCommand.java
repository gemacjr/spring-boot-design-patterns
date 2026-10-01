package com.example.designpatterns.behavioral.command;

/** <b>Concrete Command</b>: adds a quantity, and removes exactly that much on undo. */
public class AddItemCommand implements CartCommand {

    private final String sku;
    private final int quantity;

    public AddItemCommand(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    @Override public String describe() { return "add " + quantity + " x " + sku; }

    @Override public void execute(Cart cart) { cart.add(sku, quantity); }

    @Override public void undo(Cart cart) { cart.remove(sku, quantity); }
}
