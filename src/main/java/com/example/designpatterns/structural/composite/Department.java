package com.example.designpatterns.structural.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>Composite</b>: a node that delegates to its children and aggregates the answers.
 *
 * <p>Note that a department's children may themselves be departments. The
 * recursion is where the pattern earns its keep: the aggregation code is written
 * once and works to any depth.
 *
 * <p>{@link #add} returns {@code this} so trees can be built as an expression,
 * which keeps the shape of the tree visible in the shape of the code.
 *
 * <p>Full documentation: {@code docs/patterns/composite.md}
 */
public class Department implements OrgNode {

    private final String name;
    private final List<OrgNode> children = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

    public Department add(OrgNode child) {
        children.add(child);
        return this;
    }

    public List<OrgNode> children() {
        return List.copyOf(children);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int headcount() {
        return children.stream().mapToInt(OrgNode::headcount).sum();
    }

    @Override
    public BigDecimal monthlyCost() {
        return children.stream()
                .map(OrgNode::monthlyCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String render(int indent) {
        String header = " ".repeat(indent) + name + "/  [" + headcount() + " people, "
                + monthlyCost() + "/month]";
        if (children.isEmpty()) {
            return header;
        }
        return header + "\n" + children.stream()
                .map(child -> child.render(indent + 2))
                .collect(Collectors.joining("\n"));
    }
}
