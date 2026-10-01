package com.example.designpatterns.structural.adapter;

/**
 * <b>Target</b>: the interface the application wishes existed.
 *
 * <p>Note what is <em>absent</em>: no XML, no grams, no legacy type in any
 * signature. Everything the rest of the codebase touches is expressed in the
 * domain's own vocabulary.
 *
 * <p>Full documentation: {@code docs/patterns/adapter.md}
 */
public interface ShippingService {

    ShippingQuote quote(String postcode, double weightKg);
}
