package com.example.designpatterns.structural.composite;

import java.math.BigDecimal;

/**
 * <b>Leaf</b>: a node with no children.
 *
 * <p>A leaf is not a special case — it is simply a subtree of size one, which is
 * why its {@code headcount()} is 1 and its cost is its own salary.
 */
public record Employee(String name, BigDecimal salary) implements OrgNode {

    @Override
    public int headcount() {
        return 1;
    }

    @Override
    public BigDecimal monthlyCost() {
        return salary;
    }

    @Override
    public String render(int indent) {
        return " ".repeat(indent) + name + " (" + salary + ")";
    }
}
