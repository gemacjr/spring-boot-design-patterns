package com.example.designpatterns.structural.composite;

import java.math.BigDecimal;

/**
 * <b>Component</b>: the single type clients program against.
 *
 * <p>The whole payoff of Composite is that this interface says nothing about
 * whether a node has children. A client asking for {@link #headcount()} does not
 * branch on "is this a person or a department?" — the tree answers for itself.
 *
 * <p>Full documentation: {@code docs/patterns/composite.md}
 */
public interface OrgNode {

    String name();

    /** Number of people in this node, counting the whole subtree. */
    int headcount();

    /** Total monthly cost of this node, counting the whole subtree. */
    BigDecimal monthlyCost();

    /** Renders this node and its subtree, each level indented two further spaces. */
    String render(int indent);
}
