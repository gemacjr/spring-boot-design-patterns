package com.example.designpatterns.creational.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Product</b> of the Builder pattern: an immutable order request.
 *
 * <p>Why a builder rather than a constructor? A telescoping constructor with five
 * parameters &mdash; two of them optional, three of them {@code String} &mdash; is
 * unreadable at the call site and impossible to extend without breaking callers.
 * The builder names every argument and validates the <em>whole</em> object at
 * {@link Builder#build()}, where cross-field invariants can finally be checked.
 *
 * <p>The payoff: no {@code OrderRequest} anywhere in the system is ever in an
 * invalid state, so no downstream code needs a null check or a "is this order
 * complete?" guard.
 *
 * <p>Full documentation: {@code docs/patterns/builder.md}
 */
public final class OrderRequest {

    private final String customer;
    private final List<OrderLine> lines;
    private final String shipTo;
    private final boolean expedited;

    private OrderRequest(Builder builder) {
        this.customer = builder.customer;
        this.lines = List.copyOf(builder.lines);
        this.shipTo = builder.shipTo;
        this.expedited = builder.expedited;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String customer() { return customer; }

    /** Unmodifiable: the immutability of the order extends to its collections. */
    public List<OrderLine> lines() { return lines; }

    public String shipTo() { return shipTo; }

    public boolean expedited() { return expedited; }

    public BigDecimal total() {
        return lines.stream().map(OrderLine::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "OrderRequest{customer='" + customer + "', lines=" + lines.size()
                + ", shipTo='" + shipTo + "', expedited=" + expedited + ", total=" + total() + '}';
    }

    /**
     * <b>Builder</b>: mutable while assembling, discarded once {@link #build()} runs.
     *
     * <p>Each mutator returns {@code this} so calls chain. Argument-level problems
     * are rejected immediately at the offending call; whole-object problems are
     * rejected in {@code build()}, because only there is the object complete.
     */
    public static final class Builder {

        private String customer;
        private final List<OrderLine> lines = new ArrayList<>();
        private String shipTo;
        private boolean expedited;

        private Builder() {
        }

        public Builder customer(String customer) {
            this.customer = customer;
            return this;
        }

        /** Fails fast, at the call that supplied the bad value. */
        public Builder addLine(String sku, int quantity, BigDecimal unitPrice) {
            this.lines.add(new OrderLine(sku, quantity, unitPrice));
            return this;
        }

        public Builder shipTo(String shipTo) {
            this.shipTo = shipTo;
            return this;
        }

        public Builder expedited(boolean expedited) {
            this.expedited = expedited;
            return this;
        }

        /**
         * @throws IllegalStateException if the assembled order violates an invariant
         */
        public OrderRequest build() {
            if (customer == null || customer.isBlank()) {
                throw new IllegalStateException("customer is required");
            }
            if (lines.isEmpty()) {
                throw new IllegalStateException("an order needs at least one line");
            }
            if (shipTo == null || shipTo.isBlank()) {
                throw new IllegalStateException("shipTo is required");
            }
            return new OrderRequest(this);
        }
    }
}
