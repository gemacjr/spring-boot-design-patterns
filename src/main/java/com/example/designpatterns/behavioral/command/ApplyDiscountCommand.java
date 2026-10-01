package com.example.designpatterns.behavioral.command;

import java.math.BigDecimal;

/**
 * <b>Concrete Command</b>: sets a discount, remembering the previous value.
 *
 * <p>This is the memento-flavoured variant of undo: the command captures the state
 * it is about to overwrite so it can put it back.
 */
public class ApplyDiscountCommand implements CartCommand {

    private final BigDecimal percent;
    private BigDecimal previous;

    public ApplyDiscountCommand(BigDecimal percent) {
        this.percent = percent;
    }

    @Override public String describe() { return "apply " + percent + "% discount"; }

    @Override
    public void execute(Cart cart) {
        previous = cart.discountPercent();
        cart.setDiscountPercent(percent);
    }

    @Override
    public void undo(Cart cart) {
        cart.setDiscountPercent(previous);
    }
}
